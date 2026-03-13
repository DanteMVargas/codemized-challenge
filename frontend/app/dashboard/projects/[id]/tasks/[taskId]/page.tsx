'use client';

import { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { useAuth } from '@/lib/auth-context';
import api from '@/lib/api';
import { Task, Comment } from '@/lib/types';
import { ArrowLeft, Send, Trash2, Pencil, Check, X } from 'lucide-react';

const STATUS_COLORS = {
  TODO: 'bg-gray-100 text-gray-700',
  IN_PROGRESS: 'bg-blue-100 text-blue-700',
  DONE: 'bg-green-100 text-green-700',
};

export default function TaskPage() {
  const { user, loading } = useAuth();
  const router = useRouter();
  const { id, taskId } = useParams();

  const [task, setTask] = useState<Task | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [content, setContent] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const [editingCommentId, setEditingCommentId] = useState<number | null>(null);
  const [editContent, setEditContent] = useState('');

  useEffect(() => {
    if (!loading && !user) router.push('/login');
  }, [user, loading, router]);

  useEffect(() => {
    if (user && taskId) {
      fetchTask();
      fetchComments();
    }
  }, [user, taskId]);

  const fetchTask = async () => {
    try {
      const res = await api.get(`/tasks/${taskId}`);
      setTask(res.data);
    } catch {}
  };

  const fetchComments = async () => {
    try {
      const res = await api.get(`/tasks/${taskId}/comments`);
      setComments(res.data);
    } catch {}
  };

  const handleComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!content.trim()) return;
    setSubmitting(true);
    try {
      await api.post(`/tasks/${taskId}/comments`, { content });
      setContent('');
      fetchComments();
    } catch {} finally {
      setSubmitting(false);
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (!confirm('Delete this comment?')) return;
    try {
      await api.delete(`/tasks/${taskId}/comments/${commentId}`);
      fetchComments();
    } catch {}
  };

  const startEditComment = (comment: Comment) => {
    setEditingCommentId(comment.id);
    setEditContent(comment.content);
  };

  const handleEditComment = async (commentId: number) => {
    if (!editContent.trim()) return;
    try {
      await api.put(`/tasks/${taskId}/comments/${commentId}`, { content: editContent });
      setEditingCommentId(null);
      fetchComments();
    } catch {}
  };

  const handleStatusChange = async (status: string) => {
    if (!task) return;
    try {
      await api.put(`/tasks/${taskId}`, {
        title: task.title,
        description: task.description,
        status,
        assigneeId: task.assignee?.id || null,
      });
      fetchTask();
    } catch {}
  };

  if (loading || !task) return (
    <div className="min-h-screen flex items-center justify-center text-gray-500">Loading...</div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white border-b px-6 py-4 flex items-center gap-4">
        <button onClick={() => router.push(`/dashboard/projects/${id}`)} className="text-gray-500 hover:text-gray-800">
          <ArrowLeft size={20} />
        </button>
        <h1 className="text-xl font-bold text-gray-800">{task.title}</h1>
      </nav>

      <main className="max-w-3xl mx-auto px-6 py-8 space-y-6">
        <div className="bg-white rounded-xl border shadow-sm p-6">
          <div className="flex justify-between items-start">
            <div>
              <h2 className="text-lg font-semibold text-gray-800">{task.title}</h2>
              {task.description && <p className="text-gray-500 mt-2">{task.description}</p>}
              {task.assignee && <p className="text-sm text-gray-400 mt-3">Assigned to: {task.assignee.name}</p>}
            </div>
            <select
              value={task.status}
              onChange={(e) => handleStatusChange(e.target.value)}
              className={`text-sm px-3 py-1 rounded-full font-medium border-0 cursor-pointer ${STATUS_COLORS[task.status]}`}
            >
              <option value="TODO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
            </select>
          </div>
        </div>

        <div className="bg-white rounded-xl border shadow-sm p-6">
          <h3 className="font-semibold text-gray-700 mb-4">Comments ({comments.length})</h3>

          <div className="space-y-4 mb-6">
            {comments.length === 0 ? (
              <p className="text-gray-400 text-sm">No comments yet.</p>
            ) : (
              comments.map((comment) => (
                <div key={comment.id} className="border-b pb-4 last:border-0">
                  <div className="flex justify-between items-center mb-1">
                    <span className="text-sm font-medium text-gray-700">{comment.author.name}</span>
                    <div className="flex items-center gap-2">
                      <span className="text-xs text-gray-400">
                        {new Date(comment.createdAt).toLocaleDateString()}
                      </span>
                      {editingCommentId !== comment.id && (
                        <>
                          <button onClick={() => startEditComment(comment)} className="text-gray-400 hover:text-blue-500">
                            <Pencil size={13} />
                          </button>
                          <button onClick={() => handleDeleteComment(comment.id)} className="text-gray-400 hover:text-red-500">
                            <Trash2 size={13} />
                          </button>
                        </>
                      )}
                    </div>
                  </div>

                  {editingCommentId === comment.id ? (
                    <div className="flex gap-2 mt-1">
                      <input
                        type="text"
                        value={editContent}
                        onChange={(e) => setEditContent(e.target.value)}
                        className="flex-1 border border-gray-300 rounded-lg px-3 py-1 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        autoFocus
                      />
                      <button onClick={() => handleEditComment(comment.id)} className="text-green-500 hover:text-green-700">
                        <Check size={16} />
                      </button>
                      <button onClick={() => setEditingCommentId(null)} className="text-gray-400 hover:text-gray-600">
                        <X size={16} />
                      </button>
                    </div>
                  ) : (
                    <p className="text-gray-600 text-sm">{comment.content}</p>
                  )}
                </div>
              ))
            )}
          </div>

          <form onSubmit={handleComment} className="flex gap-2">
            <input
              type="text"
              placeholder="Add a comment..."
              value={content}
              onChange={(e) => setContent(e.target.value)}
              className="flex-1 border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
            />
            <button
              type="submit"
              disabled={submitting || !content.trim()}
              className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              <Send size={16} />
            </button>
          </form>
        </div>
      </main>
    </div>
  );
}