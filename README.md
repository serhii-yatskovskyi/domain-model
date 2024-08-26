# Domain Model

A lightweight library that helps to organize code according to the Domain Model architecture.

## Publishing Domain and Integration Events

1. Events should be processed in the exact order they are received. If the event E1 triggers another event E2 from
   within its handlers, the system must finish processing all handlers for E1 before it begins processing E2. This
   ensures that the events are handled in a predictable and orderly manner.
2. The `EventPublisher` is designed to stop the event publishing process if an exception occurs, to maintain the
   integrity of transactional operations. Handlers are advised to use try-catch blocks, preventing them from disrupting
   the entire event flow.
3. The `EventPublisher` is responsible for throwing an Exception to trigger a transaction's rollback.
   `DomainEventHandler`-s should be prepared for the possibility of a `RuntimeException` during the `publish` method
   execution:
   ```java
   @Component
   public class UseCase {
       @Autowired
       private EventPublisher publisher;

       @Transactional
       public void execute() {
           var events = new AggrA_Factory().createAggrA();
           publisher.publish(events); // The transaction will be rolled back, only if this method throws an exception.
       }
   }
   
   public class AggrA_Factory {
       @Autowired
       private ArgA_Repository repository;

       public Events createAggrA() {
           int id = 1;
           repository.add(new AggrA(id));
           return Events.with(new AggrA_CreatedEvent(id));
       }
   }
   
   class AggrA_CreatedEventHandler implements DomainEventHandler {

       @Override
       public void handle(DomainEvent domainEvent) throws DomainRuleViolatedException {
           if (domainEvent instanceof AggrA_CreatedEvent) {
               ...
               if (!someDomainRuleMet) {
                   throw new DomainRuleViolatedException(); // On this exception, the whole chain of changes in the
                                                            // database must be rolled back 
               }
               aggr = new AggrB();
               ...
           }
       }
   }
   ```
4. A scope of the `EventPublisher` must be regulated from the outside. For example, it can be within the scope of
   a transaction or an HTTP request. A custom Event Publisher Registry can be written, or a developer can use the
   capabilities of Spring by annotating the `EventPublisher` bean with `@Scope`. In the latter case, the
   `EventPublisher` can be declared within the Spring configuration:
   ```java
   @Configuration
   class ApplicationConfiguration {

       @Bean
       @RequestScope
       public EventPublisher eventPublisher() {
           return new EventPublisher();
       }
   }
   ```
   Alternatively, the `EventPublisher` can be inherited:
   ```java
   @Component
   @RequestScope
   class RequestScopeEventPublisher extends EventPublisher { ...
   ```
5. In some cases publishing of events must be delayed. For instance, in case events must be broadcast to
   external subscribers, in pair with Spring, the following approach can be applied:
   ```java
   @Component
   class DomainEventPropagator extends DeferredEventHandler implements TransactionSynchronization {
   
       DomainEventPropagator() {}
   
       @Override
       public void onEventDeferred(DomainEvent event) {
           TransactionSynchronizationManager.registerSynchronization(this);
       }
   
       @Override
       protected void doHandle(DomainEvent event) {
           System.out.println("The event '" + event.getClass() + "' is being propagated (not implemented).");
       }
   
       @Override
       public void afterCompletion(int status) {
           if (status == STATUS_COMMITTED) {
               handleDeferredEvents();
           }
       }
   }
   
   @Configuration
   public class Application {

       @Bean
       @RequestScope
       public EventPublisher eventPublisher(DomainEventPropagator propagator) {
           EventPublisher eventPublisher = new EventPublisher();
           eventPublisher.subscribe(propagator);
           return eventPublisher;
       }
   }
   ```
6. Saving events is fulfilled by `DomainEventSaver`-s. For Spring Framework, a subscriber implementation can be
   similar any `DomainEventPropagator` with only difference that the saving is activated before commit:
   ```java
   @Component
   class DomainEventSavers extends DeferredEventHandler implements DomainEventSaverRegistry, TransactionSynchronization {
       private final Collection<DomainEventSaver> savers;
   
       DomainEventSavers() {
           this.savers = new ArrayList<>();
       }
   
       @Override
       protected void onEventDeferred(DomainEvent event) {
           TransactionSynchronizationManager.registerSynchronization(this);
       }
   
       @Override
       protected void doHandle(DomainEvent event) {
           for (DomainEventSaver saver : savers) {
               saver.save(event);
           }
       }
   
       @Override
       public void register(DomainEventSaver saver) {
           savers.add(saver);
       }
   
       @Override
       public void beforeCommit(boolean readOnly) {
           handleDeferredEvents();
       }
   }
   
   @Configuration
   public class Application {

       @Bean
       @RequestScope
       public EventPublisher eventPublisher(DomainEventSavers savers) {
           EventPublisher eventPublisher = new EventPublisher();
           eventPublisher.subscribe(savers);
           return eventPublisher;
       }
   
       @Bean
       public DomainEventSavers domainEventSavers(ApplicationContext c) {
           DomainEventSavers s = new DomainEventSavers();
           for (DomainEventSaver saver : c.getBeansOfType(DomainEventSaver.class).values()) {
               s.register(saver);
           }
           return s;
       }
   }
   ```
<!--7. (LAB)
   It might be more safe and indeed faster to use a TreeMap registry for savers. This protects from calling
   different savers for the same event, as a result, there is a small chance that some SQL update occurs
   twice due to the same `if (domainEvent instanceof ...)` expression in different classes. A drawback of
   such approach is that a developer must always remember to register the saver for every new event... However,
   in the current approach teh developer is also required to register each new saver, more rare of cource, but
   anyway. Another benefit of a DomainEventSaverGroup is that all savers can be added to it using annotations.
   There is nothing wrong if the DomainEventSaverGroup is a singleton. Alternatively, some scanner can be
   implemented which finds all DomainEventSaver implementations within a desired package.
8.
 ```java
 DomainEventSaverGroup saver = ...
 DomainEventSaver s1 = _domainEvent -> jdbc.update("...
 saver.add(s);
 ...
 saver.save(domainEvent);
 
 class DomainEventSaverGroup {
     
     void add(DomainEventSaver s) throws SaverAlreadyRegisteredException {...
     
     void save(DomainEvent domainEvent) {
         var s = treeMap.get(domainEvent.getClass());
         s.save(domainEvent);
     }
 }

 class ElectionEventSaver implements DomainEventSaver<ElectionAnnouncedSaver> {...
 ```
It's impossible to generify savers without registration needs:
 ```java
 public class A implements I<E1>, I<E2> { // compilation error
 ```
For easy scan
 ```
 @RegisteredFor({ EventA.class, EventB.class }) // throw exception from registry for duplicates at app start
 class ElectionEventSaver implements DomainEventSaver {
 ```
How to avoid a developer forget to register saver? Use the registry inside the Infra layer!
 ```
 @Component
 class ElectionEventSaver {
     private JdbcTemplate jdbc;
     
     @PostConstruct  // Not necessary. It can be a dedicated interface
     public configure(SaverRegistryConfig r) {
         r
         .add(EventA.class, event -> {
             jdbc.update("..
         })
         .add(EventB.class, event -> {... )
     }
 }
 class SaverRegistry implements SaverRegistryConfig, DomainEventSaver {
     @Override
     void add() ...
     @Override
     void save(DomainEvent event) ...
 }
 ```-->