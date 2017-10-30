namespace weather.Models
{
    public class Noaa : IWeatherSource
    {
        public IWeatherReport fetch(int zip)
        {
            return new NoaaReport(zip, "Sunny");
        }

        private class NoaaReport : IWeatherReport
        {
            public NoaaReport(int zip, string summary)
            {
                Zip = zip;
                Summary = summary;
            }

            public int Zip { get; }

            public string Summary { get; }
        }
    }
}