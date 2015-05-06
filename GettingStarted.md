# Who should check out zaproxy-test? #

Everyone working on zaproxy or zap-extensions! Whenever you program something in one of these projects you should also provide tests for your new or changed components, classes and methods!

## Who should **not** check out zaproxy-test? ##

Pretty much everyone else, as zaproxy-test is not a standalone project of its own. It is the sub-project of Zed Attack Proxy containing all the test code.

## But why do I have to write tests in the first place? ##

You would rather spend your time on developing more new features for ZAP than writing unit tests?

  * But you want to deliver those features with as little bugs and glitches as possible?
  * And you want to be able to make changes to your feature later on without breaking something?
  * You want to be able to open somebody elses classes and be able to work on these without breaking something either?

If you answered at least one of these questions with **Yes!** then developer tests are exactly what you should write!

## Do I have to do TDD now?! ##

Of course you _can_ do Test Driven Development (TDD) and it is highly recommended to do so as a software craftsman! But TDD is not a _must_ for working on ZAP. Committing production code into ZAP without committing at least some unit tests is bad practice. But it's up to you if you do TDD or write some tests when done with a new method or class.

# How can I set-up zaproxy-test in my IDE? #

The following pages explain how to set up the zaproxy-test project and integrate it into the ZAP workspace using your favorite IDE.

  * EclipseWorkspaceSetup
  * NetbeansWorkspaceSetup
  * IntellijWorkspaceSetup
  * ViWorkspaceSetup ;-)

# What should I do before I start writing tests? #

Please read and follow the TestingGuidelines!

# I've got a problem! What now? #

  1. Check the [FAQ](FAQ.md) for a similar problem and its possible solution
  1. If the [FAQ](FAQ.md) does not help please post your problem on the [ZAP Developer Group](http://groups.google.com/group/zaproxy-develop) and we will try to help you