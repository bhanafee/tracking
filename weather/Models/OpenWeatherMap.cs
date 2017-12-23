namespace weather.Models
{
    public class OpenWeatherMap : Weather
    {
        public OpenWeatherMap(Endpoint endpoint) : base (endpoint)
        {
        }

        public override IReport ByZip(int zip)
        {
            var baseUri = BuildUri();
            // Don't worry about empty query string, because OpenWeatherMap requires APPID parameter in the base URI.
            baseUri.Query = baseUri.Query.Substring(1) + string.Format("&zip={0:00000},us", zip);
            var raw = Fetcher(baseUri.Uri);
            return new Report(raw.Result);
        }

        private class Report : IReport
        {
            public Report(string summary)
            {
                Summary = summary;
            } 
            public string Summary { get; }
        }
    }
}