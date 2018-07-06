package it.cwmp.view

import it.cwmp.controller.OpeningController
import it.cwmp.utils.{LayoutRes, StringRes}
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

class OpeningView extends View {

  var controller: OpeningController = _

  /**
    * Main method that starts this view.
    **/
  override def start(): Unit = {
    val mainStage = new Stage
    mainStage setTitle StringRes.openingTitle
    /*mainStage setHeight 400
    mainStage setWidth 600*/
    mainStage setResizable false

    val loader: FXMLLoader = new FXMLLoader(getClass.getResource(LayoutRes.openingLayout))
    val root: Pane = loader.load[Pane]
    controller = loader.getController[OpeningController]
    controller.stage = mainStage
    val scene: Scene = new Scene(root)

    mainStage.setOnCloseRequest((_) => {
      Platform.exit()
      System.exit(0)
    })
    mainStage setScene scene
    mainStage.show()
  }
}
