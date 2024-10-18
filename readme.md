# I18n Language System

The I18n API allows developers to easily create multilingual applications. It supports the localization of text and other content in various languages.

##  Installation

Add the following repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>eldonexus</id>
        <url>https://eldonexus.de/repository/maven-releases/</url>
    </repository>
</repositories>
    
<dependencies>
    <dependency>
        <groupId>de.kamiql.I18n</groupId>
        <artifactId>I18n</artifactId>
        <version>2.0.16-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```
## Usage

To initialize the I18n system, create a new instance of the `I18nProvider`. If no configuration is provided, a default configuration file will be automatically generated. The configuration file typically contains the default language and the localized messages.

```java
new I18nProvider(this)
        .initialize(YamlConfiguration config);
```

The main component of the system is the `I18n` class, which allows you to build and manage messages that adapt to the player's locale. Each message can be customized with placeholders and prefixes. The message will be fetched based on the player’s locale or fallback to a default locale if necessary.

To build a message, the `I18n.Builder` is used, allowing the configuration of placeholders, prefixes, and the actual message key. Below is an example of how to use the `I18n.Builder`:

```java 
new I18n.Builder("message_key", player)
    .hasPrefix(true)
    .withPlaceholder("PLAYER", player.getName())
    .build()
    .sendMessageAsComponent();
```

#### In this example: 

- `message_key`: The unique identifier for the message in the configuration file.
- `player`: The recipient of the message. Their locale is used to determine the appropriate translation.
- `hasPrefix(true)`: Adds a prefix to the message, either from the default configuration or a custom one.
- `withPlaceholder`: Replaces placeholders like {PLAYER} in the message with specific values.
## Configuration
The configuration file for the I18n system is written in YAML format and defines:

- Custom prefixes
- Translations for messages in multiple languages
#### Here’s an example configuration:
```yml
defaultLocale: 'en'

prefix:
  - "<gray>[<aqua>I18n<gray>] >> "

translations:
  message_key_1:
    de-de:
      - '<gray>Dies ist eine Nachricht 2</gray>'
      - '<gray>Dies ist eine Nachricht 3</gray>'
    en-us:
      - '<gray>This is a message 2</gray>'
      - '<gray>This is a message 3</gray>'
    
  message_key_2:
    de-de:
      - '<gray>Dies ist eine Nachricht mit placeholder {0}</gray>'
    en-us:
      - '<gray>This is a message with a placeholder {0}</gray>'

```
***
## Key Components:
1. `defaultLocale`: The fallback language in case a player's locale does not have a matching translation.
2. `prefix`: Optional prefixes that can be applied to messages, which can be customized or disabled for specific cases.
3. `translations`: Key-value pairs where each key corresponds to a message identifier, and each value contains translations for different locales (e.g., `en`, `de`).
***
## Placeholders

Messages can include placeholders like `{0}`, `{PLAYER}`, which will be dynamically replaced at runtime by values provided through the `Builder`. This is especially useful for inserting player names or other runtime data into messages.

Example of using placeholders in a translation:
```yaml
  message_key_2:
    de-de:
      - '<gray>Dies ist eine Nachricht mit placeholder {0}</gray>'
    en-us:
      - '<gray>This is a message with a placeholder {0}</gray>'
```
In code, you would set the placeholder value like this:
```java
new I18n.Builder("message_key_3", player)
        .withPlaceholder("0", "Custom Value")
        .build()
        .sendMessageAsComponent();
```
This would replace {0} with "Custom Value" when the message is sent.
