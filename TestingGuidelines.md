Before working on zaproxy-test please read the following page carefully and use it as a guideline when writing tests for ZAP!



# Tests #

## Unit Tests ##

Unit Tests test the smallest testable part of an application. In Java these are classes in most cases. For very big classes it might make sense to have an individual unit test for specific methods or usage scenarios of a class.

Each test case is independent from the others. Substitutes for dependencies (like fakes, stubs or mock objects) can be used to assist testing a module in isolation.

Unit Tests have several characteristics, such as
  * testing the behavior of a single class
  * no dependencies on external resources, databases, file systems, configuration settings or classpath entries
  * fast execution time

If you have a test that does not fulfill those requirements you most likely have an Integration Test at hand instead.

## Integration Tests ##

Within an Integration Test software components are combined and tested as a group. These tests are typically not executed when programming with TDD. Instead they are mostly run in a automated nightly build or before packing a JAR for potential release. Therefore they can
  * test the behavior of several classes together
  * have dependencies on external resourses, databases, file systems, configuration settings and classpath entries
  * take longer to execute

Anyway each Integration test should still be as focussed and explicit as possible.

# Test Suites #

Test suites bundle tests by their context and type. This allows for a dedicated execution of a subset of tests.

## Top Level Test Suites ##

The following test suites are pre-defined and make up the top level suites of all ZAP projects:
  * `ZaproxyUnitTestSuite` and `ZapExtensionsUnitTestSuite` bundle all unit tests
  * `ZaproxyIntegrationTestSuite` and `ZapExtensionsIntegrationTestSuite` bundle all integration tests
  * `ZaproxyAllTestSuite` bundles `ZaproxyUnitTestSuite` and `ZaproxyIntegrationTestSuite` in order to execute all tests for the main zaproxy project at once
  * `ZapExtensionsAllTestSuite` bundles `ZapExtensionsUnitTestSuite` and `ZapExtensionsIntegrationTestSuite` in order to execute all tests for the zap-extensions project at once
  * `ZapAllTestSuite` bundles `ZaproxyAllTestSuite` and `ZapExtensionsAllTestSuite` in order to execute **all** tests at once

This results in the following hierarchy of suites:

![http://zaproxy-test.googlecode.com/svn/wiki/images/zaproxy-test_suites-hierarchy.png](http://zaproxy-test.googlecode.com/svn/wiki/images/zaproxy-test_suites-hierarchy.png)

## Individual Test Suites ##

Instead of adding each individual test class to one of the above suites it might be useful to create some more specific test suites, i.e. for individual extensions (e.g. `AjaxSpiderExtensionUnitTestSuite`) or components (`DirBusterIntegrationTestSuite`).

## Highlander Principle for Test (Suite) inclusion ##

There can be only one suite that includes a given test case or another test suite. This means that it is not allowed to include a test in multiple test suites directly. Sticking to this rules makes sure that no tests are executed more than once when running one of the most top-level suites.

# Test Libraries #

## JUnit ##

JUnit is a simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.
  * [Getting started with unit testing and JUnit4](http://junit.sourceforge.net/doc/cookbook/cookbook.htm)

## Mockito ##

The Mockito library enables mocks creation, verification and stubbing. With its clean & simple API it allows to write comprehensible and beautiful tests.
  * [Getting started with Mockito](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mockito.html)

## Hamcrest ##

Hamcrest provides a library of matcher objects (also known as constraints or predicates) allowing 'match' rules to be defined declaratively, to be used inside JUnit tests.
  * [Hamcrest Tutorial](http://code.google.com/p/hamcrest/wiki/Tutorial)

## Powermock ##

Powermock is a framework that extends Mockito with more powerful capabilities. It uses a custom classloader and bytecode manipulation to enable mocking of static methods, constructors, final classes and methods, private methods, removal of static initializers and more.
  * [Using Powermock with Mockito](http://code.google.com/p/powermock/wiki/MockitoUsage13)

**Powermock should only be used as a last resort** when dealing with static utility classes or legacy code that is impossible to bring into a cleaner & more test-friendly form
  * Powermocks bytecode manipulation has a _dramatic_ impact on the ramp-up time for tests and test suites
  * Powermock must never be introduced when refactorings or the introduction of Dependency Injection are able to mitigate testing difficulties

# Naming Conventions #

In order to make tests and test suites easily distiguishable from each other the following naming conventions for test classes apply:

  * **Unit tests** have a class name matching `*UnitTest`. Examples:
    * `AliasKeyManagerUnitTest` containing tests for all the functionality offered by the `AliasKeyManager` class
    * `URLResolverRfc1808ExamplesUnitTest` containing special tests (regarding RFC1808 compliance in this case) for the `URLResolver` class in a dedicated test case
  * **Integration Tests** have a class name matching `*IntegrationTest`. Examples: `TODO`
  * **Test Methods** should be named as descriptive as possible and reflect what is expectated of the class for the given test. Examples:
    * `shouldReturnNullWhenNoCertificatesAreFound()`
    * `shouldReturnPrivateKeyFromKeyStore()`
    * `shouldConvertDataIntoCorrectBase64String()`
    * `shouldConsiderTwoNullValuesAsEqual()`
  * The method names above can be read pretty easily and convey the expectation of the test quite fine. Here are some more counterintuitive or ugly examples which should be avoided:
    * `testScenario1()`, `testScenario2()`, ...
    * `nameOfMethodUnderTest_Exception()`, `nameOfMethodUnderTest_ReturnNull()`, ...
    * `shouldWork()`, `shouldNotWork()`, ...
  * **Test Suites** other than those pre-defined (see above) are useful to execute a specific set of tests all at once. They should be named according to the test context and test type. Examples:
    * `AjaxSpiderExtensionUnitTestSuite`
    * `DirBusterIntegrationTestSuite` etc.

## Behavior Driven Development ##

For improved clarity and readability Unit Tests and Integration Tests alike should use the [Behavior Driven Development (BDD)](http://en.wikipedia.org/wiki/Behavior_Driven_Development) style of writing tests using `//given //when //then` comments as fundamental parts of all test methods.

This makes the tests easily human-readable and explain the expectations and verifications of the test better than any other method allows to:

### Simple Example ###

```
public class UtilUnitTest {

  @Test
  public void shouldPauseForGivenDuration() {
    // Given
    int intendedPause = 500;

    // When
    long startTime = System.currentTimeMillis();
    Util.sleep(intendedPause);
    long endTime = System.currentTimeMillis();
    double pause = endTime - startTime;

    // Then
    assertThat(pause, is(closeTo(intendedPause, 100d))); // allow 20% variance
  }
}
```

### Example with mocks using BDDMockito ###
```
TODO
```

# Code Quality #
  * All code in the zaproxy-test project should compile without any compiler warnings
  * Only passing tests may be committed into SVN
  * Unfinished or currently broken tests must be annotated with `@Ignore` in order to deactivate them during test suite execution
  * All code should be written following the [Clean Code](http://www.cleancoders.com) ideals

# Code Coverage #

Code Coverage is a measure used in software testing. It describes the degree to which the source code of a program has been tested.

Code Coverage in ZAP should be continuously increased with the final goal of **100%** coverage!

The following [Video compiled from two vlog posts from Robert C. Martin](https://www.dropbox.com/s/zoouelmmk24r4hz/UncleBob100CodeCoverage.wmv) explain quite nicely why setting goals of less than 100% does not make any sense, as long as you understand that 100% will always be an asymptotic goal. You just should not stop testing just because you passed an artificially defined coverage threshold of 50%, 70%, 80%, 90% or 95%.