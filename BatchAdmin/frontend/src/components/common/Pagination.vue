<template>
  <div class="text-center" v-if="paginate">
    <div class="paginate" :style="propStyle">
      <ul>
        <select class="per-page" @change="onChange($event)" v-if="isPageShow" v-model="pageCnt">
            <option :value=20>20</option>
            <option :value=50>50</option>
            <option :value=100>100</option>
        </select>
        <li class="page-item" v-for="page in paginate" :class="{on: page.active}" :key="page.id">
          <a
            :class="{ disabled: page.active === false && page.href === false }"
            @click="goPage(page.href, page.per_page)"
            >
            <i class="fas fa-angle-left" v-if="page.i == 'prev'"></i>
            <i class="fas fa-angle-right" v-else-if="page.i == 'next'"></i>
            <span v-else>{{ page.i }}</span>
          </a>
        </li>
      </ul>
    </div>

  </div>
</template>

<style scoped>

</style>

<script>
export default {
  data () {
    return {
      isPageShow: this.isPropPageCntShow,
      pageCnt: this.propPageCnt
    }
  },
  /** 한 화면에 페이징이 여러개일 경우 watch 를 써야 동기화가 됨, mounted로는 안됨 */
  watch: {
    propPageCnt: {
      handler (newVal, oldVal) {
        this.pageCnt = this.propPageCnt
      }
    }
  },
  methods: {
    goPage (page, perPage) {
      if (page === false) return false
      this.action(page, perPage) /** 부모로 action 보내기 */
    },
    onChange (event) {
      let perPage = event.target.value
      this.$parent.get(1, perPage)
    }
  },
  props: {
    'paginate': Array,
    'action': Function,
    'isPropPageCntShow': Boolean,
    'propStyle': {
      type: String,
      default: ''
    },
    'propPageCnt': {
      type: Number,
      default: 20
    }
  }
}
</script>
