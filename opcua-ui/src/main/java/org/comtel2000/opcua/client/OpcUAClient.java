/*******************************************************************************
 * Copyright (c) 2016 comtel2000
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package org.comtel2000.opcua.client;

import org.comtel2000.opcua.client.presentation.MainView;
import org.comtel2000.opcua.client.service.OpcUaClientConnector;
import org.comtel2000.opcua.client.service.PersistenceService;
import org.slf4j.LoggerFactory;

import com.airhacks.afterburner.injection.Injector;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class OpcUAClient extends Application {

  private final static org.slf4j.Logger logger = LoggerFactory.getLogger(OpcUAClient.class);

  private final DoubleProperty sceneWidthProperty = new SimpleDoubleProperty(1024);
  private final DoubleProperty sceneHeightProperty = new SimpleDoubleProperty(800);

  @Override
  public void start(Stage stage) throws Exception {

    stage.setTitle("OPC-UA client.fx (" + System.getProperty("javafx.runtime.version") + ")");
    stage.setResizable(true);

    Injector.setLogger(logger::trace);

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    lc.getLogger("org.eclipse.milo.opcua.sdk.client.subscriptions").setLevel(Level.INFO);

    PersistenceService session = Injector.instantiateModelOrService(PersistenceService.class);

    Injector.instantiateModelOrService(OpcUaClientConnector.class);

    session.bind(sceneWidthProperty, "scene.width");
    session.bind(sceneHeightProperty, "scene.height");

    MainView main = new MainView();

    final Scene scene = new Scene(main.getView(), sceneWidthProperty.get(), sceneHeightProperty.get());
    stage.setOnCloseRequest((e) -> {
      sceneWidthProperty.set(scene.getWidth());
      sceneHeightProperty.set(scene.getHeight());
      Injector.forgetAll();
      System.exit(0);
    });
    stage.setScene(scene);
    stage.getIcons().add(new Image(OpcUAClient.class.getResourceAsStream("icon.png")));
    stage.show();

  }

  public static void main(String[] args) {
    launch(args);
  }
}
