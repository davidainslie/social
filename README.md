# Social Networking API

Service exposing API that aggregates over third party Social Networking APIs such as Facebook and Twitter. Though as a demo the interactions with these third parties are stubbed.

## Installation Requirements

SBT/Scala setup. Firstly you will need [homebrew](https://brew.sh/):

```bash
$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

- Install **JDK** (Java)
- Install **sbt** (Scala)

```bash
$ brew install homebrew/cask/java
$ brew cask info java
```

```bash
$ brew install sbt
$ brew info sbt
```

To "get the code" we need [git](https://git-scm.com/).

```bash
$ brew install git
```

And then **clone** this repository:

```bash
$ git clone https://github.com/davidainslie/social.git
```

Finally, for manually testing install [httpie](https://httpie.org/), though you can of course use **curl** but it's not as nice:

```bash
$ brew install httpie
```

## Scala

**Test**

```bash
$ sbt test
```

**Run**

```bash
$ sbt run
```

However **run** in this case expects to interact with actual third party (backend) APIs, so instead we can run with stubs:

```bash
$ sbt test:run
```

## API

**Story**:

```yaml
Given a social network name Facebook
And a full Facebook graph
Return count of people with no relationships
```

From the command line:

```bash
$ http http://localhost:8080/relationships/facebook
HTTP/1.1 200 OK
Content-Length: 9
Content-Type: application/json
Date: Mon, 16 Dec 2019 09:59:33 GMT

[
    "Harry"
]
```

**Story**:

```yaml
Given a person name Peter
And a Facebook graph for Peter
And a Twitter graph for Peter
Return count of relationships of 1 degree + count of relationships of 2 degree
```

From the command line:

```bash
http http://localhost:8080/relationships/Peter
HTTP/1.1 200 OK
Content-Length: 44
Content-Type: application/json
Date: Mon, 16 Dec 2019 11:48:58 GMT

{
    "firstDegreeCount": 3,
    "secondDegreeCount": 0
}
```

