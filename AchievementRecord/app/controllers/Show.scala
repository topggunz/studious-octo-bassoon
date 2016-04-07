package controllers

import jp.t2v.lab.play2.auth.AuthElement
import models.Auth
import play.api.mvc.Controller
import views.html

/**
  * Created by Pichai Sivawat on 4/7/2016.
  */
trait Show extends Controller with Pjax with AuthElement with AuthConfigImpl {

  def profile(username: String) = StackAction(AuthorityKey -> Seq(Auth.Student, Auth.Teacher, Auth.Staff)) { implicit request =>
    val profile = Auth.valueOf(models.Account.findByUsername(username).head.role_id) match {
      case Auth.Student => models.Student.joins(models.Student.curriRef).joins(models.Student.trackRef).findAll().filter(_.student_id.value == username).head
      case Auth.Staff => models.Staff.joins(models.Staff.sectionRef).findAll().filter(_.username == username).head
      case Auth.Teacher => models.Teacher.joins(models.Teacher.statRef).findAll().filter(_.username == username).head
    }
    Ok(html.profile("Profile", profile))
  }

  protected val main: User => Template = html.main.apply
}

object Show extends Show
