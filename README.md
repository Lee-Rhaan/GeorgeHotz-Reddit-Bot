## GeorgeHotz-Reddit-Bot
|Java Core|Maven|Batch Scripting|
|---|---|---|
---
### What Is A Reddit bot? 
A Reddit bot is a program that can monitor posts, comments, and other users' actions and autonomously respond to them.

### Who is George Hotz?
- George Francis Hotz, alias geohot, is an American security hacker, entrepreneur, and software engineer.
- He is known for developing iOS jailbreaks, reverse engineering the PlayStation 3, and for the subsequent lawsuit brought against him by Sony.
- He recently was hired as an Intern by Elon Musk, with the goal of helping them refactor Twitter's code base in 12 weeks.
> The task was too big, so he resigned unfortunately.

---
## Overview:

Elon Musk was trending these past few months, so a fellow redditor decided to create a reddit bot that mimmicks Elon Musk as the Twitter CEO.

The Elon-Bot became pretty popular in one of the programming subreddits "r/ProgrammerHumour".

I've always wanted to create a bot, so when I heard George Hotz was joining Twitter, I then decided to create my own Bot and mimmick GeoHot himself.

I'm not going to lie, it was a bit difficult starting off, the Reddit API is not as clear as you'd think.
> The info on how to use the API was completely vague.

To top it off, I decided to code this bot in *Java*.
> Most if not all the Reddit Bots are usually created in Python, by using it's most popular reddit Wrapper called *PRAW*.
It's a module that provides a simple access to Reddit's API.

It's easy to use, follows all of Reddit's API rules, is well documented and there's a lot of tutorials + helpful content on the internet that can get you up to speed in no time.

All the java Reddit wrappers like Reddit4J, JRAW and JReddit was either abandoned or incomplete.
> Plus there was no tutorials or additional content available on creating the reddit bot in java.

The only thing that was well documented was the process of creating your reddit application in order to receive your reddit APP ID, SECRET etc.
> Here's the Link: https://www.reddit.com/prefs/apps

- So basically what I did was, I studied a few Reddit Bot Python implementations with the PRAW wrapper, documented a few of my findings, 
then I reverse engineered the process.
- I then ended up having to do everything manually on my side, no reddit wrappers to help me lol.

### It was an Incremental Process: 

- Had to first figure out which Reddit API Endpoints was going to be useful for the type of Bot I was planning on creating.

---

## Additional Information:

### George Hotz Reddit Bot JAR

- Contained in the target folder in this projects repo, you'll find already packaged JAR file.
- Remove it and rebuild the project (enter this command in the project directory): mvn clean package 
> NOTE: I've added the JAR's just for reference purposes.
> The same JAR file should appear once you've build the project successfuly.

The JAR:
- GeoHotBot.jar

### Batch Script

- Contained in this projects repo, you'll notice an already made batch script as well.
- Edit the script with notepad/notepad++
- Copy the location of the George Hotz Reddit Bot JAR once it has been successfuly build.
- Place the path in the {place_holder) field and save the script.

> Now you're ready to go!!!
