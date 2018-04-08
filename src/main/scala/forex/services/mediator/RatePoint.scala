package forex.services.mediator

import forex.domain.{Price, Rate}

/** Used in a store, to cache responses from a rate provider.
  *
  * @param age amount of seconds since the moment of creation or renewal.
  * @param rate data to save.
  */
case class RatePoint(age: Int, rate: Rate) {

  /** Checks the order of the rate pair names */
  val swapped: Boolean = rate.pair.swapped

  /** Gives the same id to rates and their swapped versions */
  val id: String = rate.pair.id

  /** In essence one should be able to get the price of 'y' to 'x' given the price of 'x' to 'y' by this simple formula:
    *   1 / price(x, y)
    *
    * This method computes such equivalent rate. It is used to compute rates from already cached equivalent rates.
    */
  def swap: RatePoint = copy(rate = rate.swap)
}
