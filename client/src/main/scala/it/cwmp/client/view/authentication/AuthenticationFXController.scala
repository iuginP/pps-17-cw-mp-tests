package it.cwmp.client.view.authentication

import it.cwmp.client.utils.{LayoutRes, StringRes}
import it.cwmp.client.view.{FXAlerts, FXChecks, FXController, FXView}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.stage.Stage

/**
  * Trait that models the strategy to be applied to resolve authentication requests.
  */
trait AuthenticationFXStrategy {
  /**
    * Function invoked for a system access request.
    *
    * @param username identification chosen by the player to access the system
    * @param password password chosen during sign up
    */
  def onSignIn(username: String, password: String): Unit

  /**
    * Function invoked for checking the correctness of the passwords.
    *
    * @param password password chosen
    * @param confirmPassword confirmation password
    * @return true, if the passwords respect the correctness policies
    *         false, otherwise
    */
  def onCheckPassword(password: String, confirmPassword: String): Boolean

  /**
    * Function invoked for a system registration request.
    *
    * @param username identification chosen by the player to register in the system
    * @param password password chosen to authenticate in the system
    */
  def onSignUp(username: String, password: String): Unit
}

/**
  * [[AuthenticationFXController]] companion object
  *
  * @author Elia Di Pasquale
  */
object AuthenticationFXController {
  def apply(strategy: AuthenticationFXStrategy): AuthenticationFXController = {
    require(strategy != null)
    new AuthenticationFXController(strategy)
  }
}

/**
  * Class that models the controller that manages the various authentication processes.
  *
  * @param strategy strategy to be applied to resolve authentication requests.
  */
class AuthenticationFXController(strategy: AuthenticationFXStrategy) extends FXController with FXView with FXChecks with FXAlerts {

  protected val layout: String = LayoutRes.authenticationLayout
  protected val title: String = StringRes.appName
  protected val stage: Stage = new Stage
  protected val controller: FXController = this

  @FXML
  private var tpMain: TabPane = _
  @FXML
  private var tfSignInUsername: TextField = _
  @FXML
  private var pfSignInPassword: PasswordField = _
  @FXML
  private var tfSignUpUsername: TextField = _
  @FXML
  private var pfSignUpPassword: PasswordField = _
  @FXML
  private var pfSignUpConfirmPassword: PasswordField = _


  @FXML
  private def onClickSignIn(): Unit = {
    Platform.runLater(() => {
      for(
        username <- getTextFieldValue(tfSignInUsername, "È necessario inserire lo username");
        password <- getTextFieldValue(pfSignInPassword, "È necessario inserire la password")
      ) yield strategy.onSignIn(username, password)
    })
  }

  @FXML
  private def onClickSignUp(): Unit = {
    Platform.runLater(() => {
      for(
        username <- getTextFieldValue(tfSignUpUsername, "È necessario inserire lo username");
        password <- getTextFieldValue(pfSignUpPassword, "È necessario inserire la password");
        confirmPassword <- getTextFieldValue(pfSignUpConfirmPassword, "È necessario inserire nuovamente la password")
      ) yield {
        // TODO: rivedere questa logica per farne una più specifica
        if (strategy.onCheckPassword(password, confirmPassword)) {
          strategy.onSignUp(username, password)
        } else {
          showError("Warning", "Non-compliant passwords!")
        }
      }
    })
  }

  @FXML
  private def onClickSignInReset(): Unit = {
    Platform.runLater(() => {
      resetFields()
    })
  }

  @FXML
  private def onClickSignUpReset(): Unit = {
    Platform.runLater(() => {
      resetFields()
    })
  }

  override def resetFields(): Unit = {
    if (tpMain.getSelectionModel.getSelectedIndex == 0) {
      tfSignInUsername setText ""
      pfSignInPassword setText ""
    } else {
      tfSignUpUsername setText ""
      pfSignUpPassword setText ""
      pfSignUpConfirmPassword setText ""
    }
  }

}
