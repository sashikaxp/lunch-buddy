# lunch-buddy

An application allowing users to create group sessions, invite others to join that session and allow them to submit restaurant suggestions.

The application consist of two components - a Java backend that mediates the session and submission handling with a Redis cache and a React front end for the users to interact with the backend.

## Running a development server locally
  > ***Please note you will need Docker, Java 17 and Node Js 16+ installed on your local machine in order to run this application***> 

You can start the Java backend and a local Redis instance by running the following command inside the project root directory

```D
docker-compose up --build
```


## Architecture

The architecture of the lunch-buddy application is intentionally kept simple just to address the requirements at hand. This also enables quick iteration and validation cycles. Following diagram shows the very high-level design.

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/4798bc67-cfda-46e3-a3a2-83524a724b66)

### Process and data flows
#### Main session creation

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/31718f57-6233-447b-ab4a-89389c04f91e)

As illustrated in the above diagram, when creating a main session, it will create a uniqeitem in cache and it will also create sub session items to match the number of friends in the group. 
The main items keeps track of who the member items. Each member item is also keeping a reference to the main item.

#### Submit suggestion against a sub session

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/3e745a4c-93b6-4635-be7c-dde57e0ea5d4)

Suggestions are submitted only against sub sessions.

#### Retrieve submissin details

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/4de90dd7-4971-4d2c-9365-a996adbb9704)


SUbmission details can be retrieved by using the own sub session id. It will retrieve the own submission as well as a list of other submissions fouond through the main session

#### Closing a session

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/8425836b-aeec-4914-96d6-895b89bdc5fb)




