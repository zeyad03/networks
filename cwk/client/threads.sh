#!/bin/bash

# Define the number of clients to simulate
NUM_CLIENTS=20

# Define the server host and port
SERVER_HOST="localhost"
SERVER_PORT=9100

# Define the command to be executed by the clients
COMMAND="put lipsum2.txt"

# Loop to simulate multiple client connections
for ((i=1; i<=$NUM_CLIENTS; i++)); do
    echo "Starting client $i..."
    java Client $COMMAND &
done

# Wait for all clients to finish
wait

echo "All clients finished."
