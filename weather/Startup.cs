using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using weather.Models;

namespace weather
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        public void ConfigureServices(IServiceCollection services)
        {
            services.AddSingleton<INoaaAuthentication>(new Authn(
                Configuration["Noaa:Email"],
                Configuration["Noaa:Token"]
            ));
            services.AddTransient<IWeatherSource, Noaa>();
            services.AddMvc();
        }

        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseMvc();
        }
    }

    public class Authn : INoaaAuthentication
    {
        public Authn(string email, string token)
        {
            if (string.IsNullOrEmpty(email))
            {
                throw new ArgumentException("NOAA email missing", nameof(email));
            }
            if (string.IsNullOrEmpty(token))
            {
                throw new ArgumentException("NOAA token missing", nameof(token));
            }
            Email = email;
            Token = token;
        }

        public string Email { get; }

        public string Token { get; }
    }
}