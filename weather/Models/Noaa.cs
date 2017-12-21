namespace weather.Models
{
    public interface INoaaAuthentication
    {
        string Url { get;  }
        string Email { get; }
        string Token { get; }
    }
    public class Noaa : IWeatherSource
    {
        public Noaa(INoaaAuthentication authn)
        {
            Authn = authn;
        }
        
        private INoaaAuthentication Authn { get; }

        public IWeatherReport fetch(int zip)
        {
            var summary = string.Format("For {0:d5} the weather is sunny (prepared for {1})", zip, Authn.Email);
            return new NoaaReport(zip, summary);
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