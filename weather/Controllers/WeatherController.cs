using Microsoft.AspNetCore.Mvc;
using weather.Models;

namespace weather.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController : Controller
    {
        private readonly IWeatherSource _source;

        public WeatherController(IWeatherSource source)
        {
            _source = source;
        }

        [HttpGet("{zip}")]
        public string Get(int zip)
        {
            var report = _source.fetch(zip);
            var response = string.Format("Weather for ZIP {0:d5} is {1}", report.Zip, report.Summary);
            return response;
        }
    }
}