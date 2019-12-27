<template>
  <div id="body-container">
    <div class="sideNav">
      <side-nav v-on:passSubjectId="displaySubjectDecks" v-on:passedText="searchDecksByCard"/>
    </div>
    <v-parallax src="../assets/cardsmartbackground.jpeg" style="height: 100vh; padding: 0px; positition: fixed; top: 0px; margin: 0px;">
    <div style="padding: 0px; positition: fixed; top: 0px; margin: 0px;">
    <div>
      <hello-world v-show="!isDisplayingDeck"
      />
    </div>
    <div class="card-container">
      <user-deck
        v-for="deck in decks"
        :key="deck.deckId"
        :deckName="deck.deckName"
        :deckId="deck.deckId"
        :subjectId="deck.subjectId"
        :isPublic="deck.isPublic"
        v-on:didDeckChange="displaySubjectDecks"
        class="card-format"
      />
    </div>
    </div>
    </v-parallax>
  </div>
</template>


<script>
import auth from "@/auth.js";
import SideNav from "../components/SideNav";
import UserDeck from "../components/UserDeck";
import HelloWorld from "../components/HelloWorld";

export default {
  name: "Home",
  props: {
    searchedText: String,
    searching: Boolean
  },

  components: {
    SideNav,
    UserDeck,
    HelloWorld
  },
  data() {
    return {
      searched: this.searchedText,
      isSearching: this.searching,
      change: false,
      selectedSubjectId: "",
      isDisplayingDeck: false,
      user: auth.getUser().sub,
      decks: []
    };
  },
  methods: {
    displaySubjectDecks(subjectId) {
      if (this.isSearching == true) {
        searchDecksByCard();
      } else {
        this.isDisplayingDeck = true;
        (this.selectedSubjectId = subjectId),
          fetch(
            process.env.VUE_APP_REMOTE_API +
              "/api/decks?subjectId=" +
              this.selectedSubjectId,
            {
              headers: { Authorization: "Bearer " + auth.getToken() }
            }
          )
            .then(response => response.json())
            .then(data => (this.decks = data));
      }
    },
     searchDecksByCard(searchText) {
       this.isDisplayingDeck = true;
    fetch(
      process.env.VUE_APP_REMOTE_API +
        "/api/decks?search=" +
        searchText,
      {
        headers: { Authorization: "Bearer " + auth.getToken() }
      }
    )
      .then(response => response.json())
      .then(data => (this.decks = data));
  },
  determineDisplay(){
    
  }
  }
};
</script>

<style>
#body-container {
  display: grid;
  grid-template-columns: 1fr 12fr;
  grid-template-areas: "sideNav content";
}

.sideNav {
  grid-area: sideNav;
}

.card-container {
  grid-area: content;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-self: flex-start;
  width: 100%;
}

.card-format {
  max-width: 15em;
  max-height: 10em;
  margin: 1em 0;
}
</style>
