package example

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
    val balanceIso : Iso[Account, Balance] = Iso[Account,Balance](_.balance)(balance => Account(balance.accountId, List(Booking(balance.id, balance.amount))))
    
    "transfer an account into a balance" in {
      balanceIso.get(person.account.get) === Balance(1234, 2, 64)
    }
    
    "transfer balance into an account" in {
      balanceIso.reverseGet(person.account.get.balance) === Account(1234, List(Booking(2,64)))
    }
  
  }
}