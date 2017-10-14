using Microsoft.AspNetCore.Mvc;
using weather.Models;

namespace weather.Controllers
{
    [Route("api/[controller]")]
    public class ReportController : Controller
    {

        [HttpGet("{zip}", Name = "GetReport")]
        public IActionResult GetByZip(int zip)
        {
            var result = new Report("Sunny");
            return new ObjectResult(result);
        }
    }
}