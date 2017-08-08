package com.fullLearn.endpoints.cron;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullLearn.services.FullLearnService;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


/**
 * Created by amandeep on 7/13/2017.
 */

@Path("/cron/sync/stats")
@Provider
public class UserStatsCronEndpoint {

    FullLearnService fullLearnService = new FullLearnService();

    @GET
    @Path("/daily")
    @Produces("application/json")
    public Response dailyUserStatsSync() throws IOException {


        boolean status = fullLearnService.fetchAllUserStats();
        return Response.ok().build();
    }

    @GET
    @Path("/weekly")
    @Produces("application/json")
    public Response learningStatsAverage() {

        boolean statusForAverage = fullLearnService.calculateAllUserStatsAverage();

        return Response.ok().build();
    }

    @GET
    @Path("/average")
    @Produces("application/json")
    public Response weeklyStatsReport() throws JsonProcessingException {

        boolean status = fullLearnService.generateWeeklyReport();
        if (status) {
            Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.withUrl("/task/learn/average/user/notify"));
        }

        return Response.ok().build();
    }
}

