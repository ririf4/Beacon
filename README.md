# Beacon

Beacon is a simple, lightweight, and easy-to-use library for creating and calling the event in java and kotlin.

## Installation
Latest version: ![Dynamic XML Badge](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo.ririfa.net%2Frepository%2Fmaven-public%2Fnet%2Fririfa%2Fbeacon%2Fmaven-metadata.xml&query=%2Fmetadata%2Fversioning%2Flatest&style=plastic&logo=sonatype&label=Nexus)


You need to add the repository to your build file if you're using Gradle:

Gradle(Groovy)
```groovy
repositories {
    maven { url "https://repo.ririfa.net/maven2" }
}

dependencies {
    implementation 'net.ririfa:beacon:[Version]'
}
```

Gradle(Kotlin)
```kotlin
repositories {
    maven("https://repo.ririfa.net/maven2")
}

dependencies {
    implementation("net.ririfa:beacon:[Version]")
}
```

Maven

```xml
<repositories>
  <repository>
    <id>ririfa-repo</id>
    <url>https://repo.ririfa.net/maven2</url>
  </repository>
</repositories>

<dependency>
  <groupId>net.ririfa</groupId>
  <artifactId>beacon</artifactId>
  <version>[Version]</version>
</dependency>
```

Hint: You can find all versions of the library on the [RiriFa Repo](https://repo.ririfa.net/service/rest/repository/browse/maven-public/net/ririfa/beacon/).

## Usage

1. Create a new event

Java:
```java
// Normal Event
public class MyEvent extends Event {
    public String message;
    
    public MyEvent(String message) {
        this.message = message;
    }
}

// Returnable Event
public class MyReturnableEvent extends ReturnableEvent<String> {
    public String message;
    
    public MyReturnableEvent(String message) {
        this.message = message;
    }
}
```

Kotlin:
```kotlin
// Normal Event
class MyEvent(val message: String) : Event()

// Returnable Event
class MyReturnableEvent(val message: String) : ReturnableEvent<String>()
```

2. Create an Event Listener

Java:
```java
import static net.ririfa.beacon.javaExtension.HandlerUtil.handler;
import static net.ririfa.beacon.javaExtension.HandlerUtil.returnableHandler;

public class MyEventListener implements IEventHandler {
    @Override
    public void initHandlers() {
        handler(this, MyEvent.class, event -> {
            System.out.println("This is a handler for MyEventJava: " + event.getMessage());
            System.out.println("Thread: " + Thread.currentThread().getName());
        });
        
        returnableHandler(this, MyReturnableEvent.class, event -> {
            System.out.println("This is a handler for MyReturnableEventJava: " + event.getMessage());
            System.out.println("Thread: " + Thread.currentThread().getName());
            return "Hello from returnable handler!";
        });
    }
}
```

Kotlin:
```kotlin
class MyEventListener : IEventHandler {
    override fun initHandlers() {
        handler<MyEvent> {
            println("This is a handler for MyEvent: ${it.message}")
            println("Thread: ${Thread.currentThread().name}")
        }

        returnableHandler<MyReturnableEvent> {
            println("This is a handler for MyReturnableEvent: ${it.message}")
            println("Thread: ${Thread.currentThread().name}")
            "Hello from returnable handler!"
        }
    }
}
```

3. Initialize the Event Manager and call events

Java:

```java
public class Main {
    public static void main(String[] args) {
        EventBus.initialize(new String[]{"net.ririfa.example"});
        
        EventBus.postAsync(new MyEvent("Hello from Java!"));
        
        final String result = EventBus.postReturnable(new MyReturnableEvent("Hello from Java!"), EventProcessingType.HANDLER_ASYNC);
        System.out.println("Returnable Event Result: " + result);
    }
}
```

Kotlin:

```kotlin
fun main() {
    EventBus.initialize("net.ririfa.example")
    
    EventBus.postAsync(MyEvent("Hello from Kotlin!"))
    
    val result = EventBus.postReturnable(MyReturnableEvent("Hello from Kotlin!"), EventProcessingType.HANDLER_ASYNC)
    println("Returnable Event Result: $result")
}
```

**Beacon scans within the specified package and automatically registers event listeners, so the correct package must be specified.**

## EventProcessingType
We have three different event processing types:
- FULL_SYNC: This type runs everything in the same thread as the calling thread.
- HANDLER_ASYNC: In this type, event processing takes place in a separate thread and the main thread waits for the processing. A timeout can be set, which is ideal if you want to carry out heavy processing but want the next process to take place within a specified time.
- ASYNC: In this type, all processing takes place in a separate thread and the main thread doesn't wait for the end. 

_ASYNC is not available for ‘ReturnableEvent’ with a valid return value. This is because the main thread doesn't wait for processing and may 
  access it before the return value is valid._

## Event MetaData
Event MetaData is a system that allows additional information to be stored for each event.
Dynamic keys and values can be set for each event, making it easy to customize and extend events.

※ Take Minecraft as an example :)

🔹Basic use of metadata
The method for storing and retrieving information in events is simple.
※ We still recommend using event class constructor. It is better than a meta-data system, and also it is more readable.

Java:
```java
public class MyEvent extends Event {
    public MyEvent() {
        // Set metadata
        setMeta("player", "Steve");
        setMeta("damage", 10);
    }
}

public class Main {
    public static void main(String[] args) {
        MyEvent event = new MyEvent();

        // Get metadata
        String player = (String) event.getMeta("player");
        int damage = (int) event.getMeta("damage");

        System.out.println(player + " dealt " + damage + " damage!");
    }
}
```
Output:
```
Steve dealt 10 damage!
```
---

Kotlin:
```kotlin
class MyEvent : Event() {
    init {
        setMeta("player", "Alex")
        setMeta("score", 50)
    }
}

fun main() {
    val event = MyEvent()

    val player: String = event.getMeta("player") as String
    val score: Int = event.getMetaOrDefault("score", 0)

    println("$player has a score of $score")
}
```
Output:
```
Alex has a score of 50
```
---

🔹Using metadata with CancelableEvent.
For example, metadata can be useful for canceling PvP attack events to be canceled.

Java:
```java
public class AttackEvent extends CancelableEvent {
    public AttackEvent(String attacker, String target, int damage) {
        setMeta("attacker", attacker);
        setMeta("target", target);
        setMeta("damage", damage);
    }
}

public class Main {
    public static void main(String[] args) {
        AttackEvent event = new AttackEvent("Steve", "Alex", 20);

        if (event.getMeta("target").equals("Alex")) {
            event.cancel(); // Cancel the attack for Alex
        }

        System.out.println("Attack canceled: " + event.isCanceled());
    }
}
```
Output:
```
Attack canceled: true
```

🔹Using metadata with ReturnableEvent.
<p>
By having metadata in ReturnableEvent as well,
For example, you can create Enchant Application Event.

Kotlin:
```kotlin
class EnchantEvent(item: String, level: Int) : ReturnableEvent<String>() {
    init {
        setMeta("item", item)
        setMeta("level", level)
    }
}

fun main() {
    val event = EnchantEvent("Diamond Sword", 5)

    event.setResult("Sharpness V")

    println("${event.getMeta("item")} enchanted with ${event.getResultOrDefault("Unbreaking I")}")
}
```
Output:
```
Diamond Sword enchanted with Sharpness V
```



However, we recommend using the constructor of the event class (better and more concise).