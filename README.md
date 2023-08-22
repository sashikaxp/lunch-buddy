# lunch-buddy

An application allowing users to create group sessions, invite others to join that session and allow them to submit restaurant suggestions.

The application consist of two components - a Java backend that mediates the session and submission handling with a Redis cache and a React front end for the users to interact with the backend.

Clone the app to your desired location as follows

```D
git clone git@github.com:sashikaxp/lunch-buddy.git
```

## Running a development server locally
  > ***Please note you will need Docker, Java 17, npm, yarn and Node Js 16+ installed on your local machine in order to run this application.
> Please refer to respective documentation relevant for your platform***> 

### Starting the backend
You can start the Java backend and a local Redis instance by running the following command inside the project root directory

```D
docker-compose up --build
```
This will take few minutes to complete as it will download maven dependencies and will create the docker image. Thi docker image is optimized for development and debuging. Subsequent to this initial build you can just run the 
following command if you do not have any new changes (eg new dependencies) that you think need to push into the image.

```D
docker-compose up
```

If everything is ok then application and redis instance will be up and the application will connect to the Redis instance successfully.
At this point you can start using the application API as follows

- API docs `http://localhost:8080/v3/api-docs`
- Swagger UI on API docs - `http://localhost:8080/swagger-ui/index.html#/`

### Starting the React front end

In your terminal from the project root, change the directory to `webapp/my-app/`. Then use the following command to download dependencies

```D
yarn install
```

After downloading the dependencies you can run the following command to bring up the local UI server

```D
yarn start
```

This will start the local development server hosting the UI and if successful the application will be opened in your default browser. Create a session then invite others with the invitation codes and ask them to submit restaurant suggestions. Enjoy!

### Debugging the backend

The Java backend service is started in debug mode and 5005 debug port is exposed outside the docker container. So anyone who needs to debug the app can do so by connecting to debug port.

## Architecture

The architecture of the lunch-buddy application is intentionally kept simple just to address the requirements at hand. This also enables quick iteration and validation cycles. Following diagram shows the very high-level design.

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/4798bc67-cfda-46e3-a3a2-83524a724b66)

### Process and data flows
#### Main session creation

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/31718f57-6233-447b-ab4a-89389c04f91e)

As illustrated in the above diagram, when creating a main session, it will create a uniqeitem in cache and it will also create sub session items to match the number of friends in the group. 
The main items keeps track of who the member items. Each member item is also keeping a reference to the main item.

#### Submit suggestion against a sub session

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/9605c720-ec9f-48f7-b31a-477e09ff5b12)


Suggestions are submitted only against sub sessions.

#### Retrieve submissin details

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/4de90dd7-4971-4d2c-9365-a996adbb9704)


SUbmission details can be retrieved by using the own sub session id. It will retrieve the own submission as well as a list of other submissions fouond through the main session

#### Closing a session

![image](https://github.com/sashikaxp/lunch-buddy/assets/435142/8425836b-aeec-4914-96d6-895b89bdc5fb)




