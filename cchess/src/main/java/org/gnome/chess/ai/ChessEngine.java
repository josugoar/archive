// package org.gnome.chess.ai;

// import org.gnome.chess.chess.ChessMove;

// public abstract class ChessEngine {

//     private String binary;
//     private String[] args;

//     private int delaySeconds;
//     private int pendingMoveSourceId;

//     private Pid pid = 0;
//     private int stdinFd = -1;
//     private int stderrFd = -1;
//     private IOChannel stdoutChannel;
//     private int stdoutWatchId = 0;
//     private boolean started = false;

//     protected virtual void processInput (char[] data) {}

//     public signal void starting ();

//     public signal void moved (String move);

//     public signal void resigned ();

//     public signal void stoppedUnexpectedly ();

//     public signal void error ();

//     public signal void claimDraw ();

//     public signal void offerDraw ();

//     protected ChessEngine(String binary, String[] args, int delaySeconds) {
//         this.binary = binary;
//         this.args = args;
//         this.delaySeconds = delaySeconds;
//     }

//     @Override
//     protected void finalize() throws Throwable {
//         try {
//             assert (!started);
//         } finally {
//             super.finalize();
//         }
//     }

//     public boolean start ()
//     {
//         if (pid == 0 || stdoutWatchId == 0 || stdinFd == -1 || stderrFd == -1 || !started) {
//             throw new Exception();
//         }

//         String[] argv = {binary};
//         for (var arg : args) {
//             argv += arg;
//         }
//         argv += null;

//         int stdout_fd;
//         try
//         {
//             Process.spawn_async_with_pipes (null, argv, null,
//                                             SpawnFlags.SEARCH_PATH | SpawnFlags.DO_NOT_REAP_CHILD,
//                                             null, out pid, out stdinFd, out stdout_fd, out stderrFd);
//         }
//         catch (SpawnError e)
//         {
//             warning ("Failed to execute chess engine: %s\n", e.message);
//             return false;
//         }

//         ChildWatch.add (pid, engineStoppedCb);

//         stdoutChannel = new IOChannel.unix_new (stdout_fd);
//         try
//         {
//             stdoutChannel.set_flags (IOFlags.NONBLOCK);
//         }
//         catch (IOChannelError e)
//         {
//             warning ("Failed to set input from chess engine to non-blocking: %s", e.message);
//         }
//         stdoutChannel.set_close_on_unref (true);
//         stdoutWatchId = stdoutChannel.add_watch (IOCondition.IN, readCb);

//         started = true;
//         starting ();

//         return true;
//     }

//     private void engineStoppedCb(Pid pid) {
//         /*
//          * Important: this ChildWatch callback needs to execute once for every engine
//          * process, because we rely on GLib reaping the engine immediately before
//          * executing this callback.
//          */
//         Process.closePid(pid);

//         if (this.pid == pid) {
//             stop(false);
//             stoppedUnexpectedly();
//         }
//     }

//     public abstract void startGame();

//     public abstract void reportMove(ChessMove move);

//     protected abstract void doUndo();

//     protected abstract void requestMove();

//     public void move ()
//     {
//         pendingMoveSourceId = Timeout.add_seconds (delaySeconds, () => {
//             pendingMoveSourceId = 0;
//             requestMove ();
//             return Source.REMOVE;
//         });
//     }

//     public void undo() {
//         if (pendingMoveSourceId != 0) {
//             Source.remove(pendingMoveSourceId);
//             pendingMoveSourceId = 0;
//         }

//         doUndo();
//     }

//     public void stop (boolean kill_engine = true)
//     {
//         if ((!started || stdoutChannel != null) || (!started || stdinFd != -1) || (!started || stderrFd != -1) || (!started || pid != 0)) {
//             throw new Exception();
//         }

//         if (!started)
// {            return;
// }        started = false;

//         // This can be unset on errors in readCb.
//         if (stdoutWatchId != 0) {
//             Source.remove (stdoutWatchId);
//             stdoutWatchId = 0;
//         }

//         try
//         {
//             stdoutChannel.shutdown (false);
//         }
//         catch (IOChannelError e)
//         {
//             warning ("Failed to close channel to engine's stdout: %s", e.message);
//         }
//         stdoutChannel = null;

//         if (FileUtils.close (stdinFd) == -1)
// {            warning ("Failed to close pipe to engine's stdin: %s", strerror (errno));
// }        stdinFd = -1;

//         if (FileUtils.close (stderrFd) == -1)
// {            warning ("Failed to close pipe to engine's stderr: %s", strerror (errno));
// }        stderrFd = -1;

//         if (kill_engine && Posix.kill (pid, Posix.Signal.TERM) == -1)
// {            warning ("Failed to kill engine: %s", strerror (errno));
// }        pid = 0;
//     }

//     private boolean readCb (IOChannel source, IOCondition condition)
//     {
//         if (stdoutWatchId != 0) {
//             throw new Exception();
//         }

//         char[] buf;
//         int n_read;
//         IOStatus status;

//         buf = new char[1024];
//         try
//         {
//             status = source.read_chars (buf, out n_read);
//         }
//         catch (ConvertError e)
//         {
//             warning ("Failed to read from engine: %s", e.message);
//             stdoutWatchId = 0;
//             return false;
//         }
//         catch (IOChannelError e)
//         {
//             warning ("Failed to read from engine: %s", e.message);
//             stdoutWatchId = 0;
//             return false;
//         }

//         if (status == IOStatus.EOF)
//         {
//             debug ("EOF");
//             stdoutWatchId = 0;
//             return false;
//         }
//         if (status == IOStatus.NORMAL)
//         {
//             buf.resize ((int) n_read);
//             processInput (buf);
//         }

//         return true;
//     }

//     protected void write (char[] data)
//     {
//         int offset = 0;
//         int n_written = 0;

//         do
//         {
//             n_written = Posix.write (stdinFd, &data[offset], data.length - offset);
//             offset += n_written;
//         } while (n_written > 0 && offset < data.length);
//     }

//     protected void writeLine(String line) {
//         String l = line + "\n";
//         debug("Writing line to engine: '%s'", line);
//         // getBytes("UTF8")
//         char[] d = l.to_utf8();
//         if (d != null) {
//             write(d);
//         }
//     }

// }
