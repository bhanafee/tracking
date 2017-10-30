namespace weather.Models
{
    public interface IWeatherSource
    {
        IWeatherReport fetch(int zip);
    }
    public interface IWeatherReport
    {
        int Zip { get; }
        string Summary { get; }
    }
}