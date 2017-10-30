namespace weather.Models
{
    public interface INoaaAuthentication
    {
        string Email { get; }
        string Token { get; }
    }
    public class Noaa : IWeatherSource
    {
        private readonly INoaaAuthentication _authn;
        
        public Noaa(INoaaAuthentication authn)
        {
            _authn = authn;
        } 

        public IWeatherReport fetch(int zip)
        {
            var summary = string.Format("For {0:d5} the weather is sunny (prepared for {1})", zip, _authn.Email);
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