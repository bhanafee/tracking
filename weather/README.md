# Weather service

Proxies and transforms external weather service data

## Technologies

- .NET Core


# Cloud environments

## Docker

```bash
docker build weather --tag tracking/weather
export NOAA__EMAIL='yourNoaaEmail'
export NOAA__TOKEN='yourNoaaAPIToken'
docker run -p 8080:80 tracking/weather
```

Notice there are two underscores in the environment variable names.
