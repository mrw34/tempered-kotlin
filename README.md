This a port of an existing Node.js project in order to learn more about Kotlin/Ktor. As such it lacks rigorous error handling, logging, testing, type-checking, schema validation and documentation. These are left as exercises for enthusiastic contributors.

To run the web server and a database server using Docker:

```bash
docker-compose up
```

or just the web server, without Docker:

```bash
MONGO_URL=mongodb://localhost TOKEN=abc123 gradle run
```

Then:

```bash
curl -H "Content-Type: application/json" http://localhost:8000/ -u abc123: -d '{"time": 1602422117, "temperature": 1.23, "relative humidity": 4.56}'
curl -H "Content-Type: application/json" http://localhost:8000/
```
