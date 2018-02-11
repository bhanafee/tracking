# Weather service

Proxies and transforms external weather service data

## Technologies

- .NET Core

# Usage


## By ZIP code (U.S. only)

```http://localhost/api/weather/XXXXX```

## By longitude and latitude

```http://localhost/api/weather?lat=XX.XXXX&lon=XX.XXXX```

# Cloud environments

## Docker

```bash
docker build weather --tag tracking/weather
export WEATHER__URL='yourWeatherEndpointURL'
export WEATHER__IDENTIFIER='yourWeatherEndpointUserIdentifier'
export WEATHER__KEY='yourWeatherAPIKey'
docker run -p 8080:80 tracking/weather
```

Notice there are two underscores in the environment variable names. The URL is mandatory. The identifier and key
are optional.
