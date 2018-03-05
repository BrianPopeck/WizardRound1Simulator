public class Card implements Comparable<Card>
{

	private String suit;
	private Wizard.CARD_VALUES value;

	private boolean isTrump;
	private boolean matchesLedSuit;


	public Card(String suit, Wizard.CARD_VALUES value) {
		this.suit = suit;
		this.value = value;
	}

	public Card(Wizard.CARD_VALUES type) {
		this.value = type;
		this.suit = null;
	}

	public Card copy() {
		return new Card(suit,value);
	}

	public boolean equals(Card otherCard) {
		
		if (suit!=null) {
			return (suit.equals(otherCard.getSuit()) && value.equals(otherCard.getValue()));
		} else {
			return value.equals(otherCard.getValue());
		}
		
	}

	public String getSuit() {
		return suit;
	}

	public Wizard.CARD_VALUES getValue() {
		return value;
	}

	public boolean getIsTrump() {
		return isTrump;
	}

	public void setIsTrump(boolean bool) {
		isTrump = bool;
	}

	public boolean getMatchesLedSuit() {
		return matchesLedSuit;
	}

	public void setMatchesLedSuit(boolean bool) {
		matchesLedSuit = bool;
	}

	public int compareTo(Card otherCard) {
		int returnValue = 0;

		if (this.value.equals(Wizard.CARD_VALUES.WIZARD)) {
			if (otherCard.getValue().equals(Wizard.CARD_VALUES.WIZARD)) {
				returnValue = 0;
			} else {
				returnValue = -1;
			}
		} else if (this.value.equals(Wizard.CARD_VALUES.JESTER)) {
			if (otherCard.getValue().equals(Wizard.CARD_VALUES.JESTER)) {
				returnValue = 0;
			} else {
				returnValue = 1;
			}
		} else {
			if (this.isTrump) {
				if (otherCard.getIsTrump()) {
					returnValue = this.value.compareTo(otherCard.getValue());
				} else if (otherCard.getValue().equals(Wizard.CARD_VALUES.WIZARD)) {
					returnValue = 1;
				} else {
					returnValue = -1;
				}
			} else if (this.matchesLedSuit) {
				if (otherCard.getIsTrump() || otherCard.getValue().equals(Wizard.CARD_VALUES.WIZARD)) {
					returnValue = 1;
				} else if (otherCard.getMatchesLedSuit()) {
					returnValue = this.value.compareTo(otherCard.getValue());
				} else {
					returnValue = -1;
				}
			} else { //it's not trump and it does not follow suit
				if (otherCard.getIsTrump() || otherCard.getValue().equals(Wizard.CARD_VALUES.WIZARD) || otherCard.getMatchesLedSuit()) {
					returnValue = 1;
				} else if (otherCard.getValue().equals(Wizard.CARD_VALUES.JESTER)) {
					returnValue = -1;
				} else {
					returnValue = this.value.compareTo(otherCard.getValue());
				}
			}
		}

		return returnValue;
	}
}