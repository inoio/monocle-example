package example

import scalaz.\/
import org.specs2.mutable._
import monocle.function._
import monocle._
import monocle.Iso

class ExampleSpec extends Specification with SampleData {

  import Person._ 
  import Account._
  import Address._
  
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
      val addressCity : Lens[Person, String] = Person._firstAddress composeLens Address._city
      addressCity.set("Berlin")(person) === person.copy(firstAddress = person.firstAddress.copy(city = "Berlin"))
      
      // or
      
      (_firstAddress ^|-> _city).set("Berlin")(person) === person.copy(firstAddress = person.firstAddress.copy(city = "Berlin"))
    }
  }
  
  "Iso" should {
    import monocle.syntax._
    import monocle.std._
    val accountToBalance : Iso[Account, Balance] = Iso[Account,Balance](_.balance)(balance => Account(balance.accountId, List(Booking(balance.id, balance.amount))))

    "transfer an account into a balance" in {
      val acc : Option[Account] = _account.get(person)
      accountToBalance.get(person.account.get) === Balance(1234, 2, 64)
      
      // or
      
      (person.account.get applyIso accountToBalance  get)  === Balance(1234,2, 64)
    }
    
    "transfer balance into an account" in {
      accountToBalance.reverseGet(person.account.get.balance) === Account(1234, List(Booking(2,64)))
      
      // or
      
      (person.account.get.balance applyIso accountToBalance.reverse get)  === Account(1234, List(Booking(2,64)))
    }
  }
}