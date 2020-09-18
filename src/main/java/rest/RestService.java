package rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import database.DatabaseService;
import database.model.Baum;
import database.model.Befund;
import database.model.GeoLocation;
import database.model.Spezies;
import mock.MockObjectCreator;

@Path("/")
public class RestService
{
	@Inject
	DatabaseService dao;
	@Inject
	MockObjectCreator m;
	
	
	@Path("streets/{streetId}/trees")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTreesByStreedId(@PathParam("streetId") String streetId)
	{
		int idInt;
		try
		{
			idInt = Integer.parseInt(streetId);
		}
		catch(NumberFormatException e)
		{
			return Response.status(Status.BAD_REQUEST)
					.build();
		}
		List<Baum> locations = dao.getBäumeByStrasseId(idInt);
		return Response.status(Status.OK)
				.entity(locations)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	
	@Path("streets/{streetId}/trees/geolocations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTreeCoordsByStreedId(@PathParam("streetId") String streetId)
	{
		int idInt;
		try
		{
			idInt = Integer.parseInt(streetId);
		}
		catch(NumberFormatException e)
		{
			return Response.status(Status.BAD_REQUEST)
					.build();
		}
		List<GeoLocation> locations = dao.getGeoLocationOfAllTreesByStrasseId(idInt);
		return Response.status(Status.OK)
				.entity(locations)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("trees/{treeId}/befunde")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataByTreeId(@PathParam ("treeId") String treeId)
	{
		int treeString;
		try
		{
			treeString = Integer.parseInt(treeId);
		}
		catch(NumberFormatException e)
		{
			return Response.status(Status.BAD_REQUEST)
					.entity("bad request parameter")
					.build();
		}
		List<Befund> befunde = dao.getBefundeOfBaumByBaumId(treeString);
		return Response.status(Status.OK)
				.entity(befunde)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("trees/{treeId}/age")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAgeOfTreeByTreeId(@PathParam ("treeId") String treeId)
	{
		int treeString;
		try
		{
			treeString = Integer.parseInt(treeId);
		}
		catch(NumberFormatException e)
		{
			return Response.status(Status.BAD_REQUEST)
					.entity("bad request parameter")
					.build();
		}
		int age = dao.getAgeOfBaumByBaumId(treeString);
		System.out.println("Age: " + age);
		return Response.status(Status.OK)
				.entity(age)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("trees/{treeId}/species")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSpeziesOfTreeByTreeId(@PathParam ("treeId") String treeId)
	{
		int treeString;
		try
		{
			treeString = Integer.parseInt(treeId);
		}
		catch(NumberFormatException e)
		{
			return Response.status(Status.BAD_REQUEST)
					.build();
		}
		Spezies spezies = dao.getSpeziesOfBaumByBaumId(treeString);
		return Response.status(Status.OK)
				.entity(spezies)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	@Path("mockobjects/")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response mock()
	{
		//TODO ugly ugly ugly
		m.createAndPersistMockObjects();
		//m.getBäume().stream().forEach(b -> dao.persistObject(b));
		return Response.status(Status.OK)
		.entity("mock objects created")
		.build();
	}


}
