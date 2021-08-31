# Problem

We live in the modern world, and the modern people prefer to have convenient solutions for
inconvenient routine tasks. One of such tasks is making an appointment to doctor: you can always
phone the clinic hotline, but for either introvert or lazy people this procedure might be 
a torture.

However, there is a solution. You can ask for specialist's help online. For this intention,
all you need is to visit the website of the clinic and make the medical appointment: just
choose your doctor and send request to him.

# Summary

The application represents a clinic website that 
allows a client user to request for appointment with a specialist.

Besides, the doctor easily manages the clients' requests
 and then connection from remote turns into local, in the clinic.

New client always can make an appointment online (by registering
and creating the appointment from his profile page).

# Prerequisite:

### Installed:
- [Docker](https://docs.docker.com/)
- [Git](https://git-scm.com/doc)

# Steps

### 1. Get source code

Clone source code from git
> $ git clone https://github.com/staffsterr2000/clinic-website

Go to the project root folder
> $ cd clinic-website

### 2. Run (with docker-compose)

Build and start the containers by running

> $ docker-compose -f docker-compose.yml --env-file docker.env up

Stop the containers (optional)

> $ ^C

Remove the containers (optional)

> $ docker-compose -f docker-compose.yml --env-file docker.env down

### 3. Using

3.1. Go to [website](https://localhost:8080), click **Sign up** and commit registration procedure.

3.2. Follow the [mail receiver](https://localhost:1080) link and confirm the received message to enable your client account.

3.3. Go to [your profile](https://localhost:8080/profile) and make an appointment with a doctor from list.

3.4. Doctor can either accept your visit and point its date and time, or decline it.

3.5. Go to the clinic and do visit the doctor!!

