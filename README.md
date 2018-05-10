# soccer-calendar

### What is this?
This service pulls information from a public API that serves data about football teams, matches, and leagues. It processes this data and uses it to compile a list of all upcoming matches where both teams are highly-ranked in their respective leagues. For example, you can use this service to generate a list of all future Premier League matches in which both teams are ranked in the Top 4 positions of the Premier League. The goal of this application is to notify soccer fans of any competitive matches that are occurring in the future, so they can make plans to watch the game live, record it, or follow it in some other way. You can learn more about the football-data API I used to build this application [here](http://www.football-data.org/documentation).

### How this repository is organized
The master branch will always be even with the most recent release version, and the version number will always be a SNAPSHOT. New functionality will gradually be merged into the dev branch as it is developed and tested locally. Once the dev branch has a significant feature set, it will be merged into master, and a new release branch (i.e. 1.0.0) will be cut from master.

### How do I use this?
If you want to use or extend this project, just include the following as a dependency in your Maven project:
```
<groupId>com.github.danielkesner</groupId>
<artifactId>soccer-calendar</artifactId>
<version>0.9.0-SNAPSHOT</version>
```    
To run the application, just call to Application.run().
