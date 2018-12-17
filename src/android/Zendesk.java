package com.rarestep.zendesk;

import android.content.Context;
import android.content.Intent;

import com.fleetio.go_app.MainActivity;
import com.zendesk.logger.Logger;

import junit.framework.Test;

import org.apache.cordova.*;

import org.json.JSONException;

import zendesk.commonui.UiConfig;
import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.support.Support;
import zendesk.support.guide.HelpCenterActivity;
import zendesk.support.request.RequestActivity;
import zendesk.support.requestlist.RequestListActivity;

public class Zendesk extends CordovaPlugin {
  private static final String ACTION_INITIALIZE = "initialize";
  private static final String ACTION_SET_ANONYMOUS_IDENTITY = "setAnonymousIdentity";
  private static final String ACTION_SHOW_HELP_CENTER = "showHelpCenter";
  private static final String ACTION_SHOW_TICKET_REQUEST = "showTicketRequest";
  private static final String ACTION_SHOW_USER_TICKETS = "showUserTickets";

  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext)
    throws JSONException {
    if (ACTION_INITIALIZE.equals(action)) {
      String appId = args.getString(0);
      String clientId = args.getString(1);
      String zendeskUrl = args.getString(2);

      zendesk.core.Zendesk.INSTANCE.init(this.getContext(), zendeskUrl, appId, clientId);
      Support.INSTANCE.init(zendesk.core.Zendesk.INSTANCE);
    } else if (ACTION_SET_ANONYMOUS_IDENTITY.equals(action)) {
      String name = args.getString(0);
      String email = args.getString(1);

      Identity identity = new AnonymousIdentity.Builder()
        .withNameIdentifier(name)
        .withEmailIdentifier(email)
        .build();

      zendesk.core.Zendesk.INSTANCE.setIdentity(identity);
    } else if (ACTION_SHOW_HELP_CENTER.equals(action)) {
      HelpCenterActivity.builder().show(this.getContext());
    } else if (ACTION_SHOW_TICKET_REQUEST.equals(action)) {
      RequestActivity.builder().show(this.getContext());
    } else if (ACTION_SHOW_USER_TICKETS.equals(action)) {
      RequestListActivity.builder().show(this.getContext());
    } else {
      callbackContext.error("Invalid action: " + action);
      return false;
    }

    callbackContext.success();
    return true;
  }

  private Context getContext() {
    return this.cordova.getActivity().getApplicationContext();
  }
}
