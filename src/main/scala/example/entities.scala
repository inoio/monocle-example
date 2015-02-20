package example
import scalaz.syntax.foldable._
import scalaz.std.list._
import scalaz.syntax.foldable._
import scalaz.std.math.bigDecimal._
import scalaz.std.anyVal._
import monocle.macros._

@Lenses("_")
case class Person(name: String, firstName : Option[String], firstAddress : Address , addresses : List[Address], account : Option[Account])

@Lenses("_")
case class Account(number: Int, bookings : List[Booking]) {
  def balance : Balance = Balance(number, bookings.map(_.id).maximum.getOrElse(0),  bookings.foldMap(_.amount))
}
 
@Lenses("_") 
case class Address(street: String, city: String) 

@Lenses("_")
case class Booking(id: Int, amount: BigDecimal)

@Lenses("_")
case class Balance(accountId: Int, id: Int, amount : BigDecimal)