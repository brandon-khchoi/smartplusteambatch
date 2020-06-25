<template>
  <div class="dialog-c-text">

    <div class="tableWrap tableList tableHover"
      :style="[ list.length < 8 ? { 'overflow-y':'auto', 'height': 'auto' } : { 'overflow-y':'scroll', 'height': '600px' } ]">
      <table>
        <colgroup>
          <col style="width:15%"/>
          <col style="width:25%"/>
          <col style="width:13%"/>
          <col style="width:13%"/>
          <col style="width:10%"/>
          <col style="width:15%" v-if="hasDisplaySoldoutYn"/>
        </colgroup>
        <thead>
          <th>옵션이름</th>
          <th>옵션내용</th>
          <th>추가금액</th>
          <th>재고</th>
          <th>판매상태</th>
          <th v-if="hasDisplaySoldoutYn">display_soldout_yn</th>
        </thead>
        <tbody>

          <!-- 옵션 출력 화면 -->
          <template v-if="list.length > 0" >
            <template v-for="item in list">
              <tr :key = "item.id">
                <td class="text-center">{{item.product_option_name | removeSparator}}</td>
                <td class="text-center">{{item.product_option_text | removeSparator}}</td>
                <td class="text-center">{{item.product_option_add_price | currency}}</td>
                <td class="text-center">{{item.product_option_stock_count | currency}}</td>
                <td class="text-center">

                    <template v-if="item.use_yn === '1'">
                      <!-- 품절표기유무가 null 인 경우만 count 로 품절 표기 (메이크샵)-->
                      <template v-if="item.display_soldout_yn === undefined && item.product_option_stock_count === 0 && item.use_inventory_yn === '1'">
                        품절
                      </template>
                      <!-- 카페24 -->
                      <template v-else>
                        판매중
                      </template>
                    </template>

                    <template v-else>
                      품절
                    </template>
                </td>
                <td v-if="hasDisplaySoldoutYn" class="text-center">
                    {{item.display_soldout_yn}}
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script>
// import http from '@/http-commons'
// import ModalAlert from '@/components/common/modal/ModalAlert'
// import { eventBus } from '@/main'

// MALL_DB 에 있는 추가상품(추가옵션)

export default {
  name: 'ReadSupplementProductList',
  data () {
    return {
      hasOptionStockCnt: false,
      hasDisplaySoldoutYn: false
    }
  },
  props: {
    'list': Array,
    get_option_list: {
      type: Function
    }
  },
  mounted () {
    this.checkOptionStockCnt()
    this.checkDisplaySoldoutYn()
  },
  methods: {
    checkOptionStockCnt () {
      for (const item of this.list) {
        if (item.product_option_soldout_count !== undefined) {
          this.hasOptionStockCnt = true
          break
        }
      }
    },
    checkDisplaySoldoutYn () {
      for (const item of this.list) {
        if (item.display_soldout_yn !== undefined && item.display_soldout_yn === '1') {
          this.hasDisplaySoldoutYn = true
          break
        }
      }
    }
  }

}
</script>
<style scoped>
.inputTd {
  padding: 3px 10px;
}
</style>
