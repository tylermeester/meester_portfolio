using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using UtahWildfires.Repositories;
using UtahWildfires.Models;

namespace UtahWildfires.controllers
{

    [ApiController]
    [Route("[controller]")]
    public class WildfireController : ControllerBase
    {
        private readonly IWildfireRepository _wildfireRepository;

        public WildfireController(IWildfireRepository wildfireRepository)
        {
            _wildfireRepository = wildfireRepository;
        }

        [HttpGet("/Wildfires/GetAllWildfires")]
        public async Task<ActionResult<IEnumerable<Wildfire>>> GetAllWildfires()
        {
            var wildfireData = await _wildfireRepository.GetAllWildfires();
            return Ok(wildfireData);
        }

        [HttpGet("/Wildfires/GetTenLargestWildfires")]
        public async Task<ActionResult<IEnumerable<Wildfire>>> GetTenLargestWildfires()
        {
            var wildfireData = await _wildfireRepository.GetTenLargestWildfires();
            return Ok(wildfireData);
        }

        [HttpGet("/Wildfires/GetLargestWildfire")]
        public async Task<ActionResult<IEnumerable<Wildfire>>> GetLargestWildfire()
        {
            var wildfireData = await _wildfireRepository.GetLargestWildfire();
            return Ok(wildfireData);
        }

        [HttpGet("/Wildfires/GetSmallestWildfire")]
        public async Task<ActionResult<IEnumerable<Wildfire>>> GetSmallestWildfire()
        {
            var wildfireData = await _wildfireRepository.GetSmallestWildfire();
            return Ok(wildfireData);
        }

        [HttpGet("/Wildfires/GetWildfiresByYear/{year}")]
        public async Task<ActionResult<IEnumerable<Wildfire>>> GetWildfiresByYear(int year)
        {
            var wildfireData = await _wildfireRepository.GetWildfiresByYear(new DateTime(year, 1, 1));
            return Ok(wildfireData);
        }



    }
}