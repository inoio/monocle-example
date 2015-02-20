package example

import scalaz.syntax.std.option._
import scalaz.std.option._

trait SampleData {
  
  def address = Address("Schulterblatt 36", "Hamburg")
  def account = Account(1234, Nil)
  def bookings = List(
    Booking(-64, 1),
    Booking(123, 2))
    
  def person = Person("Klink", "Markus".some, address,  Nil, account.some)
}