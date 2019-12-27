<template>
  <div @click="isTemp">
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant.sync="mini"
      permanent
      :temporary="isSideNavTemp"
      absolute
    >
      <template>
        <v-list>
          <v-list-item link>
            <v-list-item-avatar color="teal darken-1">
              <v-icon dark>mdi-account-circle</v-icon>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title 
              @click="$emit('isClicked')"
             class="subtitle-1 font-weight-bold">{{user}}</v-list-item-title>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn icon @click.stop="isMini">
                <v-icon>mdi-chevron-left</v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>
      </template>

      <v-list nav dense id="subjects">
        <v-list-item-group color="green">
          <v-list-item>
            <v-icon>mdi-magnify</v-icon>
            <v-text-field
              light
              placeholder="Search For Cards"
              single-line
              hide-details
              :searchedText="this.search"
              v-model="search"
              @click="isSearched = true"
              @keyup.enter="emitSearchText(search)"
              :searching="this.isSearched"
              class="input"
            ></v-text-field>
          </v-list-item>
          <v-list-item
            link
            v-for="(subject, index) in subjects"
            :key="subject.subjectId"
            @mouseenter="activeSubjectName=index"
            @mouseleave="activeSubjectName=''"
          >
            <v-list-item-icon>
              <v-icon small>{{emoji(subject.iconPath)}}</v-icon>
            </v-list-item-icon>
            <v-list-item-title @click="emitSubjectId(subject.subjectId)">{{subject.subjectName}}</v-list-item-title>
            <v-menu top :offset-x="offset">
              <template v-slot:activator="{ on }">
                <v-icon
                  v-on="on"
                  v-show="activeSubjectName=== index"
                  @click.stop="subjectOptions = true"
                >mdi-dots-horizontal</v-icon>
              </template>
              <v-dialog v-model="subjectOptions" max-width="300px">
                <subject-options
                  v-on:changeSubjectName="refreshSubjects"
                  :subjectsId="subject.subjectId"
                  :userId="subject.userId"
                  v-on:deleteSubjectName="refreshSubjects"
                  v-on:newDeck="refreshSubjects"
                />
              </v-dialog>
            </v-menu>
          </v-list-item>
        </v-list-item-group>
      </v-list>

      <div id="navOptions">
        <template>
          <v-list>
            <v-list-item>
              <v-row justify="center" id="quizSelection">
                <v-dialog
                  v-model="quiz"
                  fullscreen
                  hide-overlay
                  transition="dialog-bottom-transition"
                >
                  <template v-slot:activator="{ on }">
                    <v-list-item v-on="on">
                      <v-list-item-icon>
                        <v-icon>mdi-lightbulb-on-outline</v-icon>
                      </v-list-item-icon>
                      <v-list-item-title class="subtitle-2">Quiz Yourself</v-list-item-title>
                    </v-list-item>
                  </template>
                  <quiz-component @closeWindow="quiz = false" />
                </v-dialog>
              </v-row>
            </v-list-item>

            <v-list-item>
              <v-row justify="center">
                <v-dialog v-model="dialog" scrollable max-width="300px">
                  <template v-slot:activator="{ on }">
                    <v-list-item v-on="on">
                      <v-list-item-icon>
                        <v-icon>mdi-card-plus-outline</v-icon>
                      </v-list-item-icon>
                      <v-list-item-title class="subtitle-2">Add New Subject</v-list-item-title>
                    </v-list-item>
                  </template>
                  <add-subject v-on:subjectSubmitted="refreshSubjects" />
                </v-dialog>
              </v-row>
            </v-list-item>
          </v-list>
        </template>
      </div>
    </v-navigation-drawer>
  </div>
</template>

<script>
import auth from "@/auth.js";
import SubjectOptions from "@/components/SubjectOptions";
import AddSubject from "@/components/AddSubject";
import QuizComponent from "@/components/QuizComponent";

var emoji = require("node-emoji");

export default {
  components: {
    SubjectOptions,
    AddSubject,
    QuizComponent
  },
  data() {
    return {
      quiz: false,
      didChange: this.didDeckChange,
      offset: true,
      dialog: false,
      subjectOptions: false,
      drawer: true,
      mini: true,
      activeSubjectName: "",
      isClicked: false,
      hasDecks: false,
      isSideNavTemp: false,
      user: auth.getUser().sub,
      search: "",
      isSearched: false,
      subjects: [
        {
          userId: "",
          subjectId: "",
          iconPath: "",
          subjectName: ""
        }
      ]
    };
  },
  methods: {
    emoji(emojiName) {
      return emoji.emojify(`${emojiName}`);
    },
    refreshSubjects() {
      fetch(process.env.VUE_APP_REMOTE_API + "/api/subjects", {
        headers: { Authorization: "Bearer " + auth.getToken() }
      })
        .then(response => response.json())
        .then(data => (this.subjects = data));
      this.dialog = false;
      this.subjectOptions = false;
    },
    emitSubjectId(subjectId) {
      this.$emit("passSubjectId", subjectId);
      this.isSearched = false;
    },
    emitSearchText(searchtext) {
      this.$emit("passedText", searchtext);
    },
    isTemp() {
      this.isSideNavTemp = true;
    },
    isMini() {
      this.mini = !this.mini;
      this.isSideNavTemp = false;
    }
  },
  created() {
    fetch(process.env.VUE_APP_REMOTE_API + "/api/subjects", {
      headers: { Authorization: "Bearer " + auth.getToken() }
    })
      .then(response => response.json())
      .then(data => (this.subjects = data));
  }
};
</script>

<style>
#card {
  width: 300px;
  position: fixed;
  left: 20%;
  top: 50%;
}

.input {
  padding: 0 1em;
}
#subjects {
  height: 53vh;
  overflow-y: auto;
}
#navOptions {
  position: absolute;
  bottom: 0;
  width: 100%;
  background-color: white;
  border-top: 0.7px solid lightgrey;
}
</style>