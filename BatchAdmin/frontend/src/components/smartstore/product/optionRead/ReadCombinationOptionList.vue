<template>
  <div class="dialog-c-text">

    <div class="tableWrap tableList tableHover"
      :style="[ list.length < 10 ? { 'overflow-y':'auto', 'height': 'auto' } : { 'overflow-y':'scroll', 'height': '600px' } ]">
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
          <template v-if="multiple_combinationYN">
            <template v-for="optionName in combination_option_name_list">
              <th :key=optionName.id>{{optionName}}</th>
            </template>
            <th>추가금액</th>
            <th>재고<template v-if="hasOptionStockCnt">(판매중지수량)</template></th>
            <th>판매상태</th>
            <th v-if="hasDisplaySoldoutYn">display_soldout_yn</th>
          </template>
          <template v-else>
            <th>옵션이름</th>
            <th>옵션내용</th>
            <th>추가금액</th>
            <th>재고 <template v-if="hasOptionStockCnt">(판매중지수량)</template></th>
            <th>판매상태</th>
            <th v-if="hasDisplaySoldoutYn">display_soldout_yn</th>
          </template>
        </thead>
        <tbody >
          <!-- 옵션 출력 화면 -->
          <template v-if="list.length > 0" >
            <template v-for="item in list">
              <tr :key = "item.id">
                <template v-if="multiple_combinationYN">
                  <template v-for="textItem in item.combinationOptionTextList">
                    <td :key=textItem.id class="text-center">
                    {{textItem}}
                    </td>
                  </template>
                </template>
                <template v-else>
                  <td class="text-center">{{item.product_option_name | removeSparator}}</td>
                  <td class="text-center">{{item.product_option_text | removeSparator}}</td>
                </template>

                <td class="text-center">
                  {{item.product_option_add_price | currency}}
                </td>

                <td class="text-center">
                  {{item.product_option_stock_count | currency}}
                  <template v-if="item.product_option_soldout_count !== undefined">
                    ({{item.product_option_soldout_count}})
                  </template>
                </td>
                <td class="text-center">
                  <template v-if="item.use_yn === '1'">
                    <template v-if="item.product_option_soldout_count !== undefined">
                      <!-- 판매중지수량이 재고수량보다 클 경우 -->
                      <template v-if="item.product_option_soldout_count >= item.product_option_stock_count && item.use_inventory_yn === '1'">
                        품절
                      </template>
                      <template v-else>
                        판매중
                      </template>
                    </template>
                    <template v-else> <!-- 판매중지수량이 없는 경우 -->
                      <!-- 품절표기유무가 null 인 경우만 count 로 품절 표기 (메이크샵)-->
                      <template v-if="item.display_soldout_yn === undefined && item.product_option_stock_count === 0 && item.use_inventory_yn === '1'">
                        품절
                      </template>
                      <!-- 카페24 -->
                      <template v-else>
                        판매중
                      </template>
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
        <template v-else >
          <tr>
            <td colspan="5">
              옵션 내용이 없습니다.
            </td>
          </tr>
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

// MALL_DB 에 있는 조합 옵션

export default {
  name: 'ReadCombinationOptionList',
  data () {
    return {
      hasOptionStockCnt: false,
      hasDisplaySoldoutYn: false
    }
  },
  props: {
    'list': Array,
    'multiple_combinationYN': Boolean,
    'combination_option_name_list': Array,
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
input[type="text"] {
    min-height: 30px;
    height: 30px;
    font-size: 14px;
}
.inputTd {
  padding: 3px 10px;
}
</style>
