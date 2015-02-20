package example

import org.specs2.mutable._
import monocle.function._
import monocle._
import monocle.Iso
import monocle.function._
import monocle.std._

import org.specs2.scalaz.Spec

class ExampleSpec extends Spec with SampleData {

  import Person._
  import Account._
  import Address._
  import Booking._

  "PLens / Lens" should {
    "have a getter for name" in {
      _name.get(person) === "Klink"
    }

    "have a setter  for name" in {
      _name.set("Holla")(person) === person.copy(name = "Holla")
    }

    "have a modifier lens for name" in {
      _name.modify(n => n + "2")(person) === person.copy(name = "Klink2")
    }

    "can compose lenses" in {
      val addressCity: Lens[Person, String] = Person._firstAddress composeLens Address._city
      addressCity.set("Berlin")(person) === person.copy(firstAddress = person.firstAddress.copy(city = "Berlin"))

      // or

      (_firstAddress ^|-> _city).set("Berlin")(person) === person.copy(firstAddress = person.firstAddress.copy(city = "Berlin"))
    }

    "can set lists" in {
      _bookings.set(List(Booking(42, -10)))(account) === account.copy(bookings = List(Booking(42, -10)))
    }

    "can modify lists" in {
      _bookings.modify(Booking(42, -10) :: _)(account) === account.copy(bookings = Booking(42, -10) :: account.bookings)
    }

    "can modify head of list" in {
      (_bookings composeOptional headMaybe).modify(_ => Booking(1, 1))(account) === account.copy(
        bookings = Booking(1, 1) :: account.bookings.tail
      )
    }

    "modifiing head of empty list results in empty list" in {
      val accountWithoutBookings = account.copy(bookings = Nil)
      (_bookings composeOptional headMaybe).modify(_ => Booking(1, 1))(accountWithoutBookings) === account.copy(
        bookings = Nil
      )
    }

    "can modify field of head of list" in {
      val copiedAccount = account.copy(
        bookings = account.bookings.head.copy(
          amount = account.bookings.head.amount + 10
        ) :: account.bookings.tail
      )

      (_bookings composeOptional headMaybe composeLens _amount).modify(_ + 10)(account) === copiedAccount

      // or

      (_bookings ^|-? headMaybe ^|-> _amount).modify(_ + 10)(account) === copiedAccount
    }

    "can modify nth field of a list" in {
      val n = 1
      val copiedAccount = account.copy(
        bookings = account.bookings match {
          case first :: second :: tail =>
            first :: second.copy(amount = second.amount + 10) :: tail
        }
      )

      (_bookings composeOptional index(n) composeLens _amount).modify(_ + 10)(account) === copiedAccount

      // or

      (_bookings ^|-? index(n) ^|-> _amount).modify(_ + 10)(account) === copiedAccount
    }

  }

  "Working with Option/Maybe" should {
    import monocle.syntax._
    import monocle.std._

    import scalaz.Maybe
    import scalaz.syntax.maybe._
    "use lens to access optional element" in {
      some.getMaybe(_firstName.get(person)) === "Markus".just
    }
 
    "define an Optional instead" in {
      import scalaz.std.option._
      import scalaz.syntax.std.option._
      val optFirstName: Optional[Person, String] = Optional[Person, String](person => person.firstName.toMaybe)(firstName => person => person.copy(firstName = firstName.some))
      
      optFirstName.set("Dude")(person) === person.copy(firstName = "Dude".some)
      optFirstName.getMaybe(person) === "Markus".just 
    }
    
  }

  "Iso" should {
    import monocle.syntax._
    import monocle.std._
    val accountToBalance: Iso[Account, Balance] = Iso[Account, Balance](_.balance)(balance => Account(balance.accountId, List(Booking(balance.id, balance.amount))))

    "transfer an account into a balance" in {
      val acc: Option[Account] = _account.get(person)
      accountToBalance.get(person.account.get) === Balance(1234, 2, 64)

      // or

      (person.account.get &<-> accountToBalance get) === Balance(1234, 2, 64)
    }

    "transfer balance into an account" in {
      accountToBalance.reverseGet(person.account.get.balance) === Account(1234, List(Booking(2, 64)))

      // or

      (person.account.get.balance &<-> accountToBalance.reverse get) === Account(1234, List(Booking(2, 64)))
    }

  }
}