# Weather service

Proxies and transforms external weather service data

## Technologies

- .NET Core


# Cloud environments

## Docker

```bash
docker build weather --tag tracking/weather
export WEATHER__URL='yourWeatherEndpointURL'
export WEATHER__IDENTIFIER='yourWeatherEndpointUserIdentifier'
export WEATHER__KEY='yourWeatherAPIKey'
docker run -p 8080:80 tracking/weather
```

Notice there are two underscores in the environment variable names. The URL is mandatory. The identifier and 
are optional.
