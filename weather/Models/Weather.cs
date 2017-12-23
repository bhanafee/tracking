using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

namespace weather.Models
{
    public interface IReport
    {
        string Summary { get; }
    }

    public abstract class Weather
    {
        protected Endpoint Source { get; }

        protected Weather(Endpoint source)
        {
            Source = source;           
        }

        public abstract IReport ByZip(int zip);

        protected async Task<string> Fetcher(Uri uri)
        {
            var client = BuildHttpClient();
            return await client.GetStringAsync(uri);
        }

        protected HttpClient BuildHttpClient()
        {
            var client = new HttpClient();
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(
                new MediaTypeWithQualityHeaderValue("application/json"));
            return client;
        }
    }
}