<!--
  2019.07.01

  updated: Leo

  :XXX
  common.js -> sidebar toggle 제거
-->

<template>
  <div class="wrap">
    <header-menu/>
      <main
        :class="{menuClosed: !openedSidebar}"
          class="container">
        <left-menu
          :openedSidebar="openedSidebar"
          :onToggleSidebar="onToggleSidebar"
        />
        <section class="content">
          <router-view/>
        </section>
      </main>
      <Footer/>
  </div>
</template>

<script>
import { eventBus } from '../main'
import HeaderMenu from './common/HeaderMenu.vue'
import LeftMenu from './common/LeftMenu.vue'
import Footer from './common/Footer.vue'

export default {
  name: 'HelloWorld',
  components: {
    HeaderMenu,
    LeftMenu,
    Footer
  },
  data: function () {
    return {
      openedSidebar: true
    }
  },
  methods: {
    onToggleSidebar: function () {
      this.openedSidebar = !this.openedSidebar

      if (this.openedSidebar) {
        eventBus.$emit('leftMenuOpenSelectedMain')
      } else {
        eventBus.$emit('leftMenuCloseSelectedMain')
      }
    }
  }
}
</script>
