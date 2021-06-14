<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list"></pagination>

    <div class="row">
      <div v-for="course in courses" class="col-md-3">
        <div class="thumbnail search-thumbnail">
          <img v-show="!course.image" class="media-object" src="/static/image/fire.png"/>
          <img v-show="course.image" class="media-object" v-bind:src="course.image"/>
          <div class="caption">
            <div class="clearfix">
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_LEVEL | optionKV(course.level) }}</td>
              </span>
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_CHARGE | optionKV(course.charge) }}</td>
              </span>
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_STATUS | optionKV(course.status) }}</td>
              </span>
            </div>
            <h3 class="search-title">
              <a href="#" class="blue">{{ course.name }}</a>
            </h3>
            <p>{{ course.summary }}</p>
            <p>
              <span class="badge badge-info">{{ course.id }}</span>
              &nbsp;
              <span class="badge badge-info">排序：{{ course.sort }}</span>
              &nbsp;
              <span class="badge badge-info">时长：{{ course.time | formatSecond }}</span>
            </p>
            <p>
              <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa fa-book bigger-120"> 大章</i>
              </button>
              &nbsp;
              <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa fa-pencil bigger-120"> 编辑</i>
              </button>
              &nbsp;
              <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                <i class="ace-icon fa fa-trash-o bigger-120"> 删除</i>
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>

    <!--    <table id="simple-table" class="table  table-bordered table-hover">
          <thead>
          <tr>
            <th>id</th>
            <th>名称</th>
            <th>概述</th>
            <th>时长</th>
            <th>价格（元）</th>
            <th>封面</th>
            <th>级别</th>
            <th>收费</th>
            <th>状态</th>
            <th>报名数</th>
            <th>顺序</th>
            <th>讲师</th>
            <th>操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="course in courses">
            <td>{{ course.id }}</td>
            <td>{{ course.name }}</td>
            <td>{{ course.summary }}</td>
            <td>{{ course.time }}</td>
            <td>{{ course.price }}</td>
            <td>{{ course.image }}</td>
            <td>{{ COURSE_LEVEL | optionKV(course.level) }}</td>
            <td>{{ COURSE_CHARGE | optionKV(course.charge) }}</td>
            <td>{{ COURSE_STATUS | optionKV(course.status) }}</td>
            <td>{{ course.enroll }}</td>
            <td>{{ course.sort }}</td>
            <td>{{ course.teacherId }}</td>
            <td>
              <div class="btn-group">
                <button v-on:click="edit(course)" class="btn btn-xs btn-info">
                  <i class="ace-icon fa fa-pencil bigger-120"></i>
                </button>
                <button v-on:click="del(course.id)" class="btn btn-xs btn-danger ">
                  <i class="ace-icon fa fa-trash-o bigger-120"></i>
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>-->

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="course.name" type="text" class="form-control" id="name" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label for="summary" class="col-sm-2 control-label">概述</label>
                <div class="col-sm-10">
                  <input v-model="course.summary" type="text" class="form-control" id="summary" placeholder="概述">
                </div>
              </div>
              <div class="form-group">
                <label for="time" class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="course.time" type="text" class="form-control" id="time" placeholder="时长">
                </div>
              </div>
              <div class="form-group">
                <label for="price" class="col-sm-2 control-label">价格（元）</label>
                <div class="col-sm-10">
                  <input v-model="course.price" type="text" class="form-control" id="price" placeholder="价格（元）">
                </div>
              </div>
              <div class="form-group">
                <label for="image" class="col-sm-2 control-label">封面</label>
                <div class="col-sm-10">
                  <input v-model="course.image" type="text" class="form-control" id="image" placeholder="封面">
                </div>
              </div>
              <div class="form-group">
                <label for="level" class="col-sm-2 control-label">级别</label>
                <div class="col-sm-10">
                  <select v-model="course.level" class="form-control" id="level">
                    <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="charge" class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="course.charge" class="form-control" id="charge">
                    <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="status" class="col-sm-2 control-label">状态</label>
                <div class="col-sm-10">
                  <select v-model="course.status" class="form-control" id="status">
                    <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="enroll" class="col-sm-2 control-label">报名数</label>
                <div class="col-sm-10">
                  <input v-model="course.enroll" type="text" class="form-control" id="enroll" placeholder="报名数">
                </div>
              </div>
              <div class="form-group">
                <label for="sort" class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="course.sort" type="text" class="form-control" id="sort" placeholder="顺序">
                </div>
              </div>
              <div class="form-group">
                <label for="teacherId" class="col-sm-2 control-label">讲师</label>
                <div class="col-sm-10">
                  <input v-model="course.teacherId" type="text" class="form-control" id="teacherId" placeholder="讲师">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>
import Pagination from "../../components/pagination";

export default {
  components: {Pagination},
  name: "business-course",
  data: function () {
    return {
      course: {}, // 用于绑定form表单的数据
      courses: [],
      COURSE_LEVEL: COURSE_LEVEL,
      COURSE_CHARGE: COURSE_CHARGE,
      COURSE_STATUS: COURSE_STATUS
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("business-course-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    _this.list(1);
  },
  methods: {
    /**
     * 点击编辑
     * @param course
     */
    edit(course) {
      let _this = this;
      _this.course = $.extend({}, course); // 列表数据复制一份赋值给表单course，防止修改表单时把列表也修改
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.course = {};
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/list", {
        page: page,
        size: _this.$refs.pagination.size
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.courses = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.course.name, "名称")
          || !Validator.length(_this.course.name, "名称", 1, 50)
          || !Validator.length(_this.course.summary, "概述", 1, 2000)
          || !Validator.length(_this.course.image, "封面", 1, 100)
      ) {
        return;
      }
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/save", _this.course).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/business/admin/course/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    },
    /**
     * 点击 大章
     * @param course
     */
    toChapter(course) {
      let _this = this;
      SessionStorage.set("course", course);
      _this.$router.push("/business/chapter");
    },
  }
}
</script>

<style scoped>
.caption h3 {
  font-size: 20px;
}
</style>