# Dummy-Framework
### DummyFramework closely replicate Spring MVC Framework and Dependency Injection.

DummyFramework might or might not achive MVC and Dependency Injection the Spring way but overall idea is the same. 

## 1. How it works.

### 1.1 Component Scan
When the `main()` method of the application using DummyFramework is call, the framework starts the component scan by reading all the files present in the home directory of `@Component` annotation *(Usually the application class)*. The framework calls `startComponentScan(Stiring)` method of `ScanComponent` class which takes root package name as input.
This method read all the class names  present in the root package and returns list of the same.

### 1.2 Bean Creation
The supplied list class names is then given to the `ApplicationContext`. This class performs 2 functions :

    1. Creating beans.
    2. Resolving `RequestMapped` methods.

The `ApplicationContext` is like *IOC* container. It could have had performed more function but right now this Framework just do MVC and DI.
The `ApplicationContext` calls `registerBeans` methods of `BeanOperation` class to register the beans. The classes passed are converted into beans and stored in the registry to used latter by the Framework. *More on it in **Into the Code** section*
Next, `ApplicationContext` will search all the methods in every component class *(Component class are class which have annotations like `@Component`, `@Controller` or `@Service`)* for `@RequestMapping` annotation. If this annotation is present on any method then that method will be stored in `RequestMapRegistry` with the url and type of method.

Now all the beans are created and are stored in `BeanRegistry`.

