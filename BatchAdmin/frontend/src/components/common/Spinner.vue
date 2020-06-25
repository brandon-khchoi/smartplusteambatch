<template>
  <transition name="fade">
    <div
      v-if="isShow"
      class="wrapper">
      <vue-simple-spinner
        :size="size"
        :line-size="5"
        :line-bg-color="'#eee'"
        :line-fg-color="'#2196f3'"
        :speed="1.2"
        :message="message"
        :font-size="_calcfontSize"
        :text-fg-color="'#e3e5e8'"
        class="spinner" />
      <div class="overlay"></div>
    </div>
  </transition>
</template>

<script>
// https://github.com/dzwillia/vue-simple-spinner
import vueSimpleSpinner from 'vue-simple-spinner'
export default {
  name: 'Spinner',
  components: {
    'vue-simple-spinner': vueSimpleSpinner
  },
  props: {
    size: {
      type: [Number, String],
      default: 70
    },
    message: {
      type: String,
      default: 'Loading...'
    }
  },
  computed: {
    _calcfontSize: function () {
      return Math.floor((typeof this.size === 'string' ? this.size_px(this.size) : this.size) / 4)
    }
  },
  methods: {
    size_px: function (size) {
      switch (size) {
        case 'tiny': return 12
        case 'small': return 16
        case 'medium': return 32
        case 'large': return 48
        case 'big': return 64
        case 'huge': return 96
        case 'massive': return 128
        default: return 80
      }
    }
  },
  data: function () {
    return {
      isShow: false
    }
  },
  mounted () {
    this.isShow = true
  },
  beforeDestroy () {
    this.isShow = false
  }
}
</script>

<style scoped>
  .wrapper {
    pointer-events: auto;
    user-select: none;
  }
  .overlay {
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 2221;
  }
  .spinner {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 2222;
  }
  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to {
    opacity: 0;
  }
</style>
