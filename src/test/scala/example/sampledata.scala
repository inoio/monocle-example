package example

import scalaz.syntax.std.option._
import scalaz.std.option._

trait SampleData {
  
  def bookings = List(
    Booking(1, 128),
    Booking(2, -64))
    
  def address = Address("Schulterblatt 36", "Hamburg")
  def account = Account(1234, bookings)
  
    
  def person = Person("Klink", "Markus".some, address,  Nil, account.some)
}