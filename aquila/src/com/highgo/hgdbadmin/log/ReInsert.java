package com.highgo.hgdbadmin.log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import com.highgo.hgdbadmin.myutil.DBUtil;
import com.highgo.hgdbadmin.vthread.Record;

/**
 * 这个线程的主要任务 1.查出第一个Object 2.将这个Object插入到Pg中 3.插入成功，删除这个Object，插入失败，写到文件中
 * 
 * @author u
 *
 */
public class ReInsert implements Runnable {
	private Pair record = null;
	private long sleepTime = 100;
	private CountDownLatch startGate;
	private CountDownLatch endGate;

	public ReInsert(CountDownLatch endGate, CountDownLatch startGate) {
		this.startGate = startGate;
		this.endGate = endGate;
	}

	@Override
	public void run() {
		try {
			this.startGate.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			doAction();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			this.endGate.countDown();
		}

	}

	public void doAction() throws ClassNotFoundException, SQLException, IOException {
		/**
		 * 如果有活着的线程||Derby中有数据
		 * lives() > 1,有活着的线程，这时可能Derby中没有数据，但是要线程不能结束，要一直检测
		 * DerbyUtil.getSize2() != 0，说明Derby中有数据，这个时候不论是不是有线程在运行，都要继续处理。
		 */
		
		while (lives() > 1 || DerbyUtil.getSize2() != 0) {// 还有线程活着，接着干,这个1是本线程自己
			if (Constant.ROWSNUM.get() != 0) {
				try {
					record = DerbyUtil.get2();
				} catch (ClassNotFoundException | SQLException | IOException e) {
				}
				Record r = record.get();
				try {
					DBUtil.insert(r);
				} catch (SQLException e) {
					// TODO
					// 将插入失败的对象写入到最终文件中，要计数，错误超过一定的数量，就建议用户重新迁移，
					// 同时给用户一定的建议，比如数据库连接检查，表模式的修改等等
					if (e instanceof SQLException) {
						try {
//							System.out
//									.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
							Log.writeObject(r);
//							System.out
//									.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
						} catch (IOException e1) {
							// e1.printStackTrace();
						}
					}
				}
			} else {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private long lives() {
		return this.endGate.getCount();
	}
}
