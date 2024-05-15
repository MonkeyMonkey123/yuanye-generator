// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** updateComment PUT /api/comments */
export async function updateCommentUsingPut(body: API.Comments, options?: { [key: string]: any }) {
  return request<any>('/api/comments', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** addComment POST /api/comments */
export async function addCommentUsingPost(body: API.Comments, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/comments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getComment GET /api/comments/${param0} */
export async function getCommentUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCommentUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseListCommentsVO_>(`/api/comments/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** deleteComment DELETE /api/comments/${param0} */
export async function deleteCommentUsingDelete(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCommentUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseBoolean_>(`/api/comments/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}
