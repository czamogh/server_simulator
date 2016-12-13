package controllers;

import helper.CSVHelper;
import io.netty.util.internal.StringUtil;
import models.App;
import play.Logger;
import play.cache.CacheApi;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppController extends Controller {

    private static final String countKey = "count";
    private static final String xuniqParam = "xuniq";
    private static final String appIdParam = "appId";
    private static final String expiryParam = "expiry";
    private static final int requestLimit = 5;
    private static final int expiry = 60 * 60;

    private CacheApi cacheApi;
    private final FormFactory formFactory;
    private final JPAApi jpaApi;

    @Inject
    public AppController(FormFactory formFactory, JPAApi jpaApi, CacheApi cache) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
        this.cacheApi = cache;
    }

    public Result index() {
        List<App> apps = CSVHelper.read();

        if(apps == null)
            apps = new ArrayList<>();

        return ok(views.html.index.render(apps));
    }

    public Result addApp() {
        App app = formFactory.form(App.class).bindFromRequest().get();
        CSVHelper.writeCsv(app);
        return redirect(routes.AppController.index());
    }

    public Result getDeferredDeepLink1() {
        if(!isValidRequest())
            return ok("");

        String id = request().getQueryString(appIdParam);
        String dl = CSVHelper.getDeepLink(id);

        return ok(jsonizeDeepLink(dl));
    }

    public Result getDeferredDeepLink2() {
        if(!isValidRequest())
            return ok("");

        String id = request().getQueryString(appIdParam);
        Integer count = cacheApi.get(countKey);

        if(count == null) {
            cacheApi.set(countKey, 1, expiry);
            return ok("");
        } else if(count + 1 == requestLimit) {
            cacheApi.set(countKey, 0, expiry);
            String dl = CSVHelper.getDeepLink(id);
            return ok(jsonizeDeepLink(dl));
        } else {
            cacheApi.set(countKey, count + 1, expiry);
            return ok("");
        }
    }

    public Result reset() {
        cacheApi.remove(countKey);
        return ok("Sucessfully reset");
    }

    public Result getDeferredDeepLink3() {
        return ok("");
    }

    private boolean isValidRequest() {
        if(request().getQueryString(appIdParam) == null ||
                request().getQueryString(xuniqParam) == null ||
                request().getQueryString(expiryParam) == null) {

            Logger.info("Not a valid request, missing some parameter");
            return false;
        }

        return true;
    }

    private String jsonizeDeepLink(String dl) {
        if(StringUtil.isNullOrEmpty(dl))
            return "";

        return "{\"deeplink\":\"" + dl + "\"}";
    }
}
